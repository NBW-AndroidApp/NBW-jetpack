package org.nieghborhoodbikeworks.nbw.ui.queue;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class QueueAdminFragment extends Fragment implements QueueAdapterAdmin.ClickListener {
    private static String TAG = "Queue Admin Fragment";
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
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode = null;

    public static QueueAdminFragment newInstance() { return new QueueAdminFragment(); }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
     *
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
        mAdapter = new QueueAdapterAdmin(getActivity(), displayQueue,
                new QueueAdapterAdmin.ClickListener() {
            @Override
            public void onItemClicked(int position) {
                if (actionMode != null) {
                    toggleSelection(position);
                }
            }

            @Override
            public boolean onItemLongClicked(int position) {
                if (actionMode == null) {
                    actionMode = ((AppCompatActivity)getActivity()).
                            startSupportActionMode(actionModeCallback);
                }

                toggleSelection(position);

                return true;
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mEnqueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If there is no user currently signed in, mUser.getName() will throw a NPE
                try {
                    mViewModel.enqueueUser();
                    updateQueue();
                } catch (NullPointerException e) {
                    Navigation.findNavController(mView).navigate(R.id.loginFragment);
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
                    Navigation.findNavController(mView).navigate(R.id.loginFragment);
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
                mAdapter = new QueueAdapterAdmin(getActivity(), displayQueue,
                        new QueueAdapterAdmin.ClickListener() {
                    @Override
                    public void onItemClicked(int position) {
                        if (actionMode != null) {
                            toggleSelection(position);
                        }
                    }

                    @Override
                    public boolean onItemLongClicked(int position) {
                        if (actionMode == null) {
                            // Starts the ActionModeCallback after a longClick with the user that
                            // was longClicked as the first user in the selectedUsers list
                            actionMode = ((AppCompatActivity)getActivity()).
                                    startSupportActionMode(actionModeCallback);
                        }

                        toggleSelection(position);

                        return true;
                    }
                });
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

    /**
     * onItemClicked will only run when the ActionMode is running, at which point the users
     * that are tapped on will be added to the selectedUsers list. Otherwise, item clicks
     * will be ignored.
     *
     * @param position
     */
    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    /**
     * Starts the ActionModeCallback.
     *
     * @param position
     * @return
     */
    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            // Starts the ActionModeCallback after a longClick with the user that
            // was longClicked as the first user in the selectedUsers list
            actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);

        return true;
    }

    /**
     * toggleSelection keeps track of the users that have been selected to be dequeue'd
     * and displays the number of users currently selected in the ActionBar.
     *
     * @param position
     */
    public void toggleSelection(int position) {
        ((QueueAdapterAdmin) mAdapter).toggleSelection(position);
        int count = ((QueueAdapterAdmin) mAdapter).getSelectedItemCount();

        if (count == 0) {
            // If there are no users currently selected, terminate the ActionModeCallback
            actionMode.finish();
        } else {
            actionMode.setTitle("Users selected: " + String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        /**
         * Run once on initial creation of the ActionMode. This method sets the xml layout
         * for the selection process; the menu layout sets the action buttons for this
         * ActionMode.
         *
         * @param mode
         * @param menu
         * @return
         */
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Sets the ActionBar for the selection process
            mode.getMenuInflater().inflate (R.menu.selected_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * Run any time a contextual action button in the ActionMode layout is clicked.
         * In our case, the following method is run when the "remove from queue" button
         * is clicked.
         *
         * @param mode
         * @param item
         * @return
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    List<Integer> selectedUsers = ((QueueAdapterAdmin) mAdapter).getSelectedItems();
                    for(Integer position : selectedUsers) {
                        String userToBeRemoved = mQueue.get(position);
                        // Remove the user from the database
                        mViewModel.dequeueUser(userToBeRemoved);
                    }
                    // Remove users from the RecyclerView on the UI
                    ((QueueAdapterAdmin) mAdapter).removeItems(selectedUsers);
                    // updateQueue sets the appropriate data in the local queue
                    updateQueue();
                    mode.finish();
                    Toast.makeText(getActivity(), "User(s) removed from the queue!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }

        /**
         * The following method will run when the selection process is cancelled.
         *
         * @param mode
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ((QueueAdapterAdmin) mAdapter).clearSelection();
            actionMode = null;
        }
    }

}