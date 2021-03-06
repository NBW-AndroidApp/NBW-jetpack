package org.nieghborhoodbikeworks.nbw.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

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

public class LoginFragment extends Fragment {
    private String TAG = "LoginFragment";
    private SharedViewModel mViewModel;
    private FirebaseAuth mAuth;
    private Button mLogInButton;
    private EditText mEmailText, mPasswordText;
    private TextView mForgotPassword, mSignUp;
    private View mView;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
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

        // TODO: Set onClickListeners for new account and forgot password

        return mView;
    }

    /**
     * The bulk of the work happens here.
     *
     * There are two onClickListeners here.
     * 1. mLogInButton:
     *      when the user presses this button, the method checks if the user has entered
     *      email and password. If this check passes, then {@link FirebaseAuth} FirebaseAuth tries
     *      to sign them in with the credentials provided, and gives feedback based on how the
     *      sign in task went.
     *
     *      If the sign in is complete, the method initiates a callback for fetching the user's
     *      data. This callback is situated in {@link SharedViewModel} since we need to wait till the
     *      data is fetched to take further actions.
     *      Once the user's data is loaded, we check if the user has already signed a waiver.
     *      If the waiver has been signed, then an {@link AlertDialog} is shown, which presents the
     *      user with options to enqueue themselves or watch orientation videos if they like.
     *      If the user hasn't signed the waiver, they're taken teo the waiver signing page to complete
     *      the signing.
     *
     * 2. mSignUpButton: whenever this button is pressed, the user is taken to the sign up page.
     *
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

        final String[] uid = new String[1];
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = mEmailText.getText().toString();
                String password = mPasswordText.getText().toString();

                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(getActivity(), "Please enter e-mail and password.",
                            Toast.LENGTH_SHORT).show();
                }

                else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(getActivity(),
                                                "Signed in!", Toast.LENGTH_SHORT).show();
                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        mViewModel.fetchUser(
                                                uid, new SharedViewModel.userDetailCallback() {
                                                    @Override
                                                    public void onCallback(User user) {
                                                        if (user.isSignedWaiver()) {
                                                            Navigation.findNavController(v).
                                                                    navigate(R.id.userChoiceFragment);
                                                        } else {
                                                            Navigation.findNavController(v).
                                                                    navigate(R.id.waiverFragment);
                                                        }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(getActivity(), task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Taking user to signup fragment.");
                // Empty these fields before switching fragments.
                mEmailText.setText("");
                mPasswordText.setText("");

                Navigation.findNavController(v).
                        navigate(R.id.signupFragment);
            }
        });
    }
}
