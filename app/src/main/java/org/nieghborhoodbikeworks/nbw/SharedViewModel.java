package org.nieghborhoodbikeworks.nbw;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String TAG = "SharedViewModel";
    private String mEmail;
    private String mPassword;
    private FirebaseAuth mAuth;
    private Task mSignInTask;

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
}
