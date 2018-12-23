package org.nieghborhoodbikeworks.nbw;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
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
    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public boolean isUserWaiverStatus() {
        return mUserWaiverStatus;
    }

    public void setUserWaiverStatus(boolean userWaiverStatus) {
        mUserWaiverStatus = userWaiverStatus;
        final FirebaseUser user = mAuth.getCurrentUser();
        mUserDatabase.child("users").child(user.getUid()).child("signedWaiver")
                .setValue(userWaiverStatus);
    }

    private boolean mNewUserStatus;

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

    /**
     * Creates a user in {@link FirebaseDatabase} Firebase Realtime Database given a newly registered User.
     * @param user
     *
     */
    public void createDatabaseUser(User user) {
        mUserDatabase.child(user.getUid()).setValue(user);
    }
}
