package org.nieghborhoodbikeworks.nbw;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private LinkedList<FirebaseUser> mQueue = new LinkedList<>();
    private MutableLiveData<LinkedList<FirebaseUser>> liveData;
    private User mUser;


    /*================================================================================
    Getters and setters
    ==================================================================================*/
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
    /*================================================================================
    End of getters and setters
    ==================================================================================*/


    /**
     * Returns an {@link AuthResult} task that determines whether the user was signed in
     * successfully or not.
     * @param email
     * @param password
     * @return
     */
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

    /**
     * Given a UID, fetches the corresponding Database user and saves that user to mUser, and
     * triggers a callback function via interface{@link userDetailCallback} for the UI thread
     * to implement to allow listening for the event completion before moving on to doing other
     * things.
     * @param uid
     * @return
     */
    public void fetchUser(String uid, final userDetailCallback dataFetchCallback) {
        mUserDatabase.child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d(TAG, "User exists in remote!");
                            mUser = dataSnapshot.getValue(User.class);
                            dataFetchCallback.onCallback(mUser);
                        }
                        else {
                            Log.d(TAG, "user doesn't exist.");
                            mUser = null;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "Can't fetch Firebase Database user:" + databaseError.getMessage());
                        mUser = null;
                        dataFetchCallback.onCallback(mUser);
                    }
                });
    }

    /**
     * Interface to listen for callback once the data about a user is fetched from Firebase
     * Database.
     */
    public interface userDetailCallback {
        void onCallback(User user);
    }
}
