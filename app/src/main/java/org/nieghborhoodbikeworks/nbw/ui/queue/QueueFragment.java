package org.nieghborhoodbikeworks.nbw.ui.queue;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import static androidx.constraintlayout.widget.StateSet.TAG;

public class QueueFragment extends Fragment {

    private SharedViewModel mViewModel;

    public QueueFragment newInstance() { return new QueueFragment(); }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private AlertDialog.Builder alertDialogBuilder;
    private Observer mObserver;
    private TextView displayQueue;
    private ArrayList<String> mQueue;
    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.queue_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        displayQueue = view.findViewById(R.id.textView4);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        databaseReference = mDatabase.getReference();
        mDatabase = mViewModel.getDatabase();
        ValueEventListener queueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mQueue = (ArrayList<String>) dataSnapshot.child("queue").getValue();
                Log.d(TAG, "verifying");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadQueue:onCancelled", databaseError.toException());
            }
        };
        databaseReference.addValueEventListener(queueListener);

        alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Queue")
                .setMessage("Would you like to add yourself to the queue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                      some logic for when the user decides to be added to the queue
//                      databaseReference.child("queue").updateChildren();
                      databaseReference.child("queue").setValue(user);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                      some logic for when the user decides to not be added to the queue
                    }
                });

        alertDialogBuilder.show();

//        mObserver = new Observer<LinkedList<FirebaseUser>>() {
//            @Override
//            public void onChanged(@Nullable final LinkedList<FirebaseUser> updatedQueue) {
//                // Update the UI
//                displayQueue.setText(this.updateUI(updatedQueue));
//            }
//
//            private String updateUI(LinkedList<FirebaseUser> updatedQueue) {
//                String result;
//                for(FirebaseUser user:updatedQueue) {
//                    user.getDisplayName();
//                }
//                return result;
//            }
//        };
//
//        mLiveData.observe(getActivity(), mObserver);

    }

}

