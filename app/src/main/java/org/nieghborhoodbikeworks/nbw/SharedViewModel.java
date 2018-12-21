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

    public boolean isUserWaiverStatus() {
        return mUserWaiverStatus;
    }

    public void setUserWaiverStatus(boolean userWaiverStatus) {
        mUserWaiverStatus = userWaiverStatus;
        final FirebaseUser user = mAuth.getCurrentUser();
        mUserDatabase.child("users").child(user.getUid()).child("signedWaiver")
                .setValue(userWaiverStatus);
    }

    public boolean isNewUserStatus() {
        return mNewUserStatus;
    }

    public void setNewUserStatus(boolean newUserStatus) {
        mNewUserStatus = newUserStatus;
    }

    private boolean mNewUserStatus;

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

    public void checkDatabaseUser(final FirebaseUser user) {
        mUserDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mUserDatabase.child(user.getUid()).child("name").setValue(user.getDisplayName());
                    mUserDatabase.child(user.getUid()).child("email").setValue(user.getEmail());
                    mUserDatabase.child(user.getUid()).child("signedWaiver").setValue(false);
                    mUserDatabase.child(user.getUid()).child("newUser").setValue(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Can't create new user table: ", databaseError.toException());
            }
        });
    }

    public boolean checkWaiverStatus(final FirebaseUser user) {
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

    public boolean checkFirstUser(final FirebaseUser user) {
        final boolean[] status = {false};
        mUserDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status[0] = (boolean) dataSnapshot.child(user.getUid()).child("newUser").getValue();
                mNewUserStatus = status[0];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to fetch status");
            }
        });
        return status[0];
    }
}
