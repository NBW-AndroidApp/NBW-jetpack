package org.nieghborhoodbikeworks.nbw.ui.queue;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class QueueFragment extends Fragment {
    private SharedViewModel mViewModel;
    private View mView;
    private DatabaseReference mQueueDatabase;
    private User mUser;
    private Button mEnqueueButton, mDequeueButton;
    private TextView mWaiting;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mQueue;
    private ArrayList<String> displayQueue;

    public static QueueFragment newInstance() { return new QueueFragment(); }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Sign-in Queue");
        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.queue_fragment, container, false);

        // Initialize queue UI elements
        mEnqueueButton = mView.findViewById(R.id.enqueue_button);
        mDequeueButton = mView.findViewById(R.id.dequeue_button);
        mWaiting = mView.findViewById(R.id.waiting);
        mRecyclerView = mView.findViewById(R.id.queue_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return mView;
    }

    /**
     * There are two OnClickListeners here:
     *
     * 1. mEnqueueButton:
     *      when the user presses this button, the method adds them to the "queue" node in the
     *      database if they are signed in. A ValueEventListener is attached to the "queue" node
     *      which is what relays the data in the queue to the RecyclerView. The RecyclerView populates
     *      its list items from the ValueEventListener so that the users already in the queue are shown
     *      once the view for this fragment is inflated.
     *
     *      If the user is not signed in and they tap on this button, a NullPointerException is
     *      thrown, at which point the user is redirected to theLoginFragment and is prompted to
     *      sign in.
     *
     * 2. mDequeueButton:
     *      when the user presses this button, the method removes them from the "queue" node if they
     *      are in it. Just like in the mEnqueueButton method, this method uses the same ValueEventListener
     *      the former method uses.
     *
     *      If the user is not signed in and they tap on this button, they will be redirected to the
     *      LoginFragment.
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Fetch data for queue
        mQueueDatabase = mViewModel.getmQueueDatabase();
        mUser = mViewModel.getUser();
        mQueue = mViewModel.getmQueue();
        displayQueue = new ArrayList<>();
        updateQueue();
        mAdapter = new QueueAdapter(getActivity(), displayQueue);
        mRecyclerView.setAdapter(mAdapter);

        mEnqueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there is no user currently signed in, mUser.getName() will throw a NPE
                try {
                    mViewModel.enqueueUser();
                    updateQueue();
                } catch (NullPointerException e) {
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("externalFragmentMessage", "enqueue");
                    Navigation.findNavController(mView).navigate(R.id.loginFragment, bundle);
                }
            }
        });

        mDequeueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there is no user currently signed in, mUser.getName() will throw a NPE
                try {
                    mViewModel.dequeueUser();
                    updateQueue();
                } catch (NullPointerException e) {
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("externalFragmentMessage", "dequeue");
                    Navigation.findNavController(mView).navigate(R.id.loginFragment, bundle);
                }
            }
        });

    }

    /**
     * The updateQueue method reads from the queue node in the database and adds the users to the mQueue
     * ArrayList.
     */
    //TODO: Move this method into the SharedViewModel
    private void updateQueue() {
        // Reads the data on the "queue" node in the database
        ValueEventListener queueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                ArrayList<String> updatedQueue = new ArrayList<>();
                displayQueue = new ArrayList<>();
                // Iterates through the nodes in the queue
                for (DataSnapshot ds1 : dataSnapshot1.getChildren()) {
                    // key = mUser.getUid(), value = mUser.getName()
                    String key = String.valueOf(ds1.getKey());
                    String value = String.valueOf(ds1.getValue());
                    // Add users in the database queue to local queue
                    updatedQueue.add(key);
                    displayQueue.add(value);
                }

                int i = 0;
                boolean userDequeued = false;

                if (mQueue.size() > updatedQueue.size()) {
                    for (String user : updatedQueue) {
                        if (!(user.equals(mQueue.get(i)))) {
                            userDequeued = true;
                            break;
                        }
                        i++;
                    }
                }

                mQueue = updatedQueue;
                if (userDequeued) {
                    mAdapter.notifyItemRemoved(i);
                }
                //TODO: Find a better way to update the RecyclerView Adapter
                mAdapter = new QueueAdapter(getActivity(), displayQueue);
                mRecyclerView.setAdapter(mAdapter);
                mWaiting.setText("Number of people currently in the queue: " +
                        String.valueOf(mQueue.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadQueue:onCancelled", databaseError.toException());
            }
        };
        mQueueDatabase.addValueEventListener(queueListener);

    }

}