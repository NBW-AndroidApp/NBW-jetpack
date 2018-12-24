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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.StateSet.TAG;

public class QueueFragment extends Fragment {

    private SharedViewModel mViewModel;

    public QueueFragment newInstance() { return new QueueFragment(); }

    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private AlertDialog.Builder alertDialogBuilder;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.queue_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatabase = mViewModel.getDatabase();
        databaseReference = mDatabase.getReference();

        alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Queue")
                .setMessage("Would you like to add yourself to the queue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("Add " + mUser.getDisplayName() + " to the queue",
                                mUser);
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                // iterates through the nodes in the queue
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    // key = "Add " + user.getDisplayName() + " to the queue"
                    String key = ds.getKey();

                    DatabaseReference users = databaseReference.child("queue").child(key);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // iterates through the users in each node in the queue
                            for(DataSnapshot user : dataSnapshot.getChildren()) {
                                // returns the users display name
                                String username = user.child("displayName").getValue(String.class);
                                // TODO: Update UI with queue
                                System.out.println(username);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    };
                    users.addListenerForSingleValueEvent(eventListener);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadQueue:onCancelled", databaseError.toException());
            }
        };
        databaseReference.child("queue").addValueEventListener(queueListener);
    }

}

