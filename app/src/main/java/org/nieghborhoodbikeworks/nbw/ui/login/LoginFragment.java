package org.nieghborhoodbikeworks.nbw.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;
import org.nieghborhoodbikeworks.nbw.ui.signup.SignUpFragment;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    private String TAG = "LoginFragment";
    private SharedViewModel mViewModel;
    private FirebaseAuth mAuth;
    private Button mLogInButton;
    private EditText mEmailText, mPasswordText;
    private TextView mForgotPassword, mSignUp;
    private AlertDialog.Builder mAlertDialog;
    private View mView;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * onCreateView initializes variables tied to the text elements in the View.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Login to NBW");
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.login_fragment, container, false);

        // Set button for logging in
        mLogInButton = mView.findViewById(R.id.login_button);
        mEmailText = mView.findViewById(R.id.email);
        mPasswordText = mView.findViewById(R.id.password);
        mSignUp = mView.findViewById(R.id.sign_up);
        mForgotPassword = mView.findViewById(R.id.forgot_password);

        // To Set onClickListeners for new account and forgot password

        return mView;
    }

    /**
     * The bulk of the work happens here.
     *
     * The very first thing that happens once the user clicks
     * Once log in button is pressed, we trigger a task that signs the user in with their
     * email and password. If the sign in fails, then we show a toast that tells the user to
     * try again.
     *
     * Once sign in task is complete, its listener starts a task to
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // TODO: Use the ViewModel
        mAuth = mViewModel.getAuth();
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Toast.makeText(
                    getActivity(),
                    currentUser.getEmail(),
                    Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "User isn't logged in!",
                    Toast.LENGTH_SHORT).show();
        }

        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = mEmailText.getText().toString();
                String password = mPasswordText.getText().toString();

                Task task = mViewModel.signIn(email, password).addOnCompleteListener
                        (new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getActivity(),
                                    "Signed in!", Toast.LENGTH_SHORT).show();
                            String uid = mViewModel.getAuth().getUid();
                            mViewModel.fetchUser(uid, new SharedViewModel.MyCallback() {
                                @Override
                                public void onCallback(User user) {
                                    if (user != null) {
                                        if (user.hasSignedWaiver()) {
                                            mAlertDialog = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Sign-in Successful!")
                                                    .setMessage("What would you like to do?")
                                                    .setPositiveButton("Add me to the queue",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog,
                                                                                    int which) {
                                                                    Navigation.findNavController(v).
                                                                            navigate(R.id.queueFragment);
                                                                }
                                                            })
                                                    .setNegativeButton("Watch orientation videos",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog,
                                                                                    int which) {
                                                                    Navigation.findNavController(v).
                                                                            navigate(R.id.orientationFragment);
                                                                }
                                                            });
                                            mAlertDialog.show();
                                        }
                                        else {
                                            Navigation.findNavController(v).navigate(R.id.waiverFragment);
                                        }
                                    }

                                    else {
                                        Toast.makeText(getActivity(), "User doesn't exist in database!" +
                                                "Please contact an administrator.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Auth failed. Check log!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Redirecting them to signup fragment");
                Navigation.findNavController(v).navigate(R.id.signupFragment);
            }
        });
    }

}