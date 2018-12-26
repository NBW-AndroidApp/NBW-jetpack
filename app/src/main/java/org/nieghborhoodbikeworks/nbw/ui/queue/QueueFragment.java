package org.nieghborhoodbikeworks.nbw.ui.queue;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class QueueFragment extends Fragment {

    private SharedViewModel mViewModel;

    public QueueFragment newInstance() { return new QueueFragment(); }

    private User mUser;
    private AlertDialog.Builder alertDialogBuilder;
    private FirebaseDatabase mDatabase = mViewModel.getDatabase();
    private DatabaseReference databaseReference = mDatabase.getReference();
    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> mQueue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.queue_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        mUser = mViewModel.getUser();
        //mQueue = mViewModel.getmQueue();
        mRecyclerView = view.findViewById(R.id.queue_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mQueue = new ArrayList<String>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // the alert dialog should be buttons

        alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Queue")
                .setMessage("Would you like to add yourself to the queue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(mUser.getName(), mUser);
                        databaseReference.child("queue").updateChildren(childUpdates);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(view).navigate(R.id.orientationFragment);
                    }
                });

        alertDialogBuilder.show();

        ValueEventListener queueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                // iterates through the nodes in the queue
                for(DataSnapshot ds1 : dataSnapshot1.getChildren()) {
                    // key = mUser.getName(), value = mUser
                    String key = ds1.getKey();
                    if(!key.equals("empty")) {
                        mQueue.add(key);
                    }
                }
                mAdapter = new QueueAdapter(getActivity(), mQueue);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadQueue:onCancelled", databaseError.toException());
            }
        };
        databaseReference.child("queue").addValueEventListener(queueListener);
    }

}

