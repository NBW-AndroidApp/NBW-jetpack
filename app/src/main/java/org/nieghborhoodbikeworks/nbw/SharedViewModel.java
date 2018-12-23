package org.nieghborhoodbikeworks.nbw;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.nieghborhoodbikeworks.nbw.ui.signup.SignUpFragment;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String TAG = "SharedViewModel";
    private String mEmail;
    private String mPassword;
    private FirebaseAuth mAuth;
    private Task mSignInTask;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mUserDatabase = mDatabase.getReference().child("users");
    private boolean mUserWaiverStatus;
    private LinkedList<FirebaseUser> mQueue = new LinkedList<FirebaseUser>();
    private MutableLiveData<LinkedList<FirebaseUser>> liveData;
    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    public void setDatabase(FirebaseDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public MutableLiveData<LinkedList<FirebaseUser>> getLiveData() {
        return liveData;
    }

    public void setLiveData(MutableLiveData<LinkedList<FirebaseUser>> liveData) {
        this.liveData = liveData;
    }

    public LinkedList<FirebaseUser> getQueue() {
        return mQueue;
    }

    public void setQueue(LinkedList<FirebaseUser> mQueue) {
        this.mQueue = mQueue;
    }

    public boolean checkUserWaiverStatus() {
        return mUserWaiverStatus;
    }

    public void setUserWaiverStatus(boolean userWaiverStatus) {
        mUserWaiverStatus = userWaiverStatus;
        final FirebaseUser user = mAuth.getCurrentUser();
        mUserDatabase.child("users").child(user.getUid()).child("signedWaiver")
                .setValue(userWaiverStatus);
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public FirebaseAuth getAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    public void setAuth(FirebaseAuth auth) {
        mAuth = auth;
    }

    public Task signIn(String email, String password) {
        if (mSignInTask == null) {
            mSignInTask = mAuth.signInWithEmailAndPassword(email, password);
        }
        return mSignInTask;
    }

    public boolean checkWaiverStatus(final User user) {
        final boolean[] status = {false};
        mUserDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status[0] = (boolean) dataSnapshot.child(user.getUid()).child("signedWaiver").getValue();
                mUserWaiverStatus = status[0];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to fetch status");
            }
        });
        return status[0];
    }

    /**
     * Creates a user in {@link FirebaseDatabase} Firebase Realtime Database given a newly registered User.
     * @param user
     *
     */
    public void createDatabaseUser(User user) {
        mUserDatabase.child(user.getUid()).setValue(user);
    }
}
