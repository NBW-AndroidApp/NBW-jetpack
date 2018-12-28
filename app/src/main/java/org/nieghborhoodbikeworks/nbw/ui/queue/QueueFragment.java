package org.nieghborhoodbikeworks.nbw.ui.queue;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class QueueFragment extends Fragment {
    private SharedViewModel mViewModel;
    private View view;
    private DatabaseReference mQueueDatabase;
    private User mUser;
    private Button mEnqueueButton;
    private Button mDequeueButton;
    private TextView mWaiting;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mQueue;

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
        ((MainActivity) getActivity()).setActionBarTitle("Queue");
        // Get the view from fragment XML
        view = inflater.inflate(R.layout.queue_fragment, container, false);

        // Initialize queue UI elements
        mEnqueueButton = view.findViewById(R.id.enqueue_button);
        mDequeueButton = view.findViewById(R.id.dequeue_button);
        mWaiting = view.findViewById(R.id.waiting);
        mRecyclerView = view.findViewById(R.id.queue_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    /**
     * There is one OnClickListener here:
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
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Fetch data for queue
        //mQueue = mViewModel.getmQueue();
        mQueueDatabase = mViewModel.getmQueueDatabase();
        mUser = mViewModel.getUser();
        mQueue = mViewModel.getmQueue();
        mAdapter = new QueueAdapter(getActivity(), mQueue);
        mRecyclerView.setAdapter(mAdapter);

        mEnqueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there is no user currently signed in, mUser.getName() will throw a NPE
                try {
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(mUser.getName(), mUser);
                    mQueueDatabase.updateChildren(childUpdates);
                    updateQueue();
//                    FirebaseAuth.getInstance().signOut();
//                    mUser = null;
                } catch (NullPointerException e) {
                    // Redirect the user to the login screen if they aren't signed in and would
                    // like to be added to, or removed from, the queue
                    Navigation.findNavController(view).navigate(R.id.loginFragment);
                }
            }
        });

        mDequeueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mQueueDatabase.child(mUser.getName()).removeValue();
                    updateQueue();
//                    FirebaseAuth.getInstance().signOut();
//                    mUser = null;
                } catch (NullPointerException e) {
                    Navigation.findNavController(view).navigate(R.id.loginFragment);
                }
            }
        });

    }

    private void updateQueue() {
        // Reads the data on the "queue" node in the database
        ValueEventListener queueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                ArrayList<String> updatedQueue = new ArrayList<>();
                // Iterates through the nodes in the queue
                for (DataSnapshot ds1 : dataSnapshot1.getChildren()) {
                    // key = mUser.getName(), value = mUser
                    String key = ds1.getKey();
                    // Add users in the queue
                    if(!key.equals("empty")) {
                        updatedQueue.add(key);
                    }
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
                mWaiting.setText("People currently in the queue: " + String.valueOf(mQueue.size()));
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadQueue:onCancelled", databaseError.toException());
            }
        };
        mQueueDatabase.addValueEventListener(queueListener);
    }

}

