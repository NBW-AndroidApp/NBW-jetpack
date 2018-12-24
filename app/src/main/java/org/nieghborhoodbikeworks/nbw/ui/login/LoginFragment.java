package org.nieghborhoodbikeworks.nbw.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
    private User mUser;
    private AlertDialog.Builder mAlertDialog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Welcome to NBW");
        // Get the view from fragment XML
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        // Set button for logging in
        mLogInButton = v.findViewById(R.id.login_button);
        mEmailText = v.findViewById(R.id.email);
        mPasswordText = v.findViewById(R.id.password);
        mSignUp = v.findViewById(R.id.sign_up);
        mForgotPassword = v.findViewById(R.id.forgot_password);

        // To Set onClickListeners for new account and forgot password

        return v;
    }

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
                mViewModel.signIn(email, password).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getActivity(),
                                    "Signed in!", Toast.LENGTH_SHORT).show();
//                            mUser = mViewModel.getAuth().getCurrentUser();
//                            if (!mViewModel.checkWaiverStatus(mUser)) {
//                                Navigation.findNavController(v).navigate(R.id.waiverFragment);
//                            } else {
//                                mAlertDialog = new AlertDialog.Builder(getActivity())
//                                        .setTitle("Sign-in Successful!")
//                                        .setMessage("What would you like to do?")
//                                        .setPositiveButton("Add me to the queue",
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog,
//                                                                        int which) {
//                                                        Navigation.findNavController(v).
//                                                                navigate(R.id.queueFragment);
//                                                    }
//                                                })
//                                        .setNegativeButton("Watch orientation videos",
//                                                new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog,
//                                                                        int which) {
//                                                        Navigation.findNavController(v).
//                                                                navigate(R.id.orientationFragment);
//                                                    }
//                                                });
//                                mAlertDialog.show();
//                            }
                        }
                        else {
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
                Log.d(TAG, "Here!");
                Navigation.findNavController(v).navigate(R.id.signupFragment);
            }
        });
    }

}
