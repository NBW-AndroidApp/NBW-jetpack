package org.nieghborhoodbikeworks.nbw.ui.login;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

public class LoginFragment extends Fragment {
    private static String TAG = "LoginFragment";
    private SharedViewModel mViewModel;
    private NavigationView mNavigationView;
    private FirebaseAuth mAuth;
    private Button mLogInButton;
    private EditText mEmailText, mPasswordText;
    private TextView mForgotPassword, mSignUp;
    private View mView;
    private CharSequence externalFragmentMessage;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
     *
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
        mNavigationView = getActivity().findViewById(R.id.nav_view);

        // Set button for logging in
        mLogInButton = mView.findViewById(R.id.login_button);
        mEmailText = mView.findViewById(R.id.email);
        mPasswordText = mView.findViewById(R.id.password);
        mSignUp = mView.findViewById(R.id.sign_up);
        mForgotPassword = mView.findViewById(R.id.forgot_password);
        externalFragmentMessage = getArguments().getCharSequence("externalFragmentMessage");

        // TODO: Set onClickListener for forgot password

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

        mAuth = mViewModel.getAuth();

        if (mViewModel.getUser() == null) {
            Toast.makeText(getActivity(), "User isn't logged in!",
                    Toast.LENGTH_SHORT).show();

            final String[] uid = new String[1];
            mLogInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String email = mEmailText.getText().toString();
                    String password = mPasswordText.getText().toString();

                    if (email.length() == 0 || password.length() == 0) {
                        Toast.makeText(getActivity(), "Please enter e-mail and password.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Hide the nav menu for when the user is signed out and show
                                            // the menu for when the user is signed in
                                            mNavigationView.getMenu().setGroupVisible(R.id.signedOut, false);
                                            mNavigationView.getMenu().setGroupVisible(R.id.signedIn, true);

                                            Log.d(TAG, "signInWithEmail:success");
                                            Toast.makeText(getActivity(),
                                                    "Signed in!", Toast.LENGTH_SHORT).show();
                                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            mViewModel.fetchUser(
                                                    uid, new SharedViewModel.userDetailCallback() {
                                                        @Override
                                                        public void onCallback(User user) {

                                                            // If the user signed the waiver
                                                            if (user.isSignedWaiver()) {

                                                                // TODO: Replace the following "if" statement with
                                                                // "switch" statement

                                                                if (externalFragmentMessage != null) {

                                                                    // If the user pressed enqueue somewhere
                                                                    // in the app but wasn't logged in
                                                                    if (externalFragmentMessage.equals("enqueue")) {
                                                                        mViewModel.enqueueUser();
                                                                        Navigation.findNavController(v).
                                                                                navigate(R.id.queueFragment);
                                                                        Toast.makeText(getActivity(),
                                                                                "Added to the queue!",
                                                                                Toast.LENGTH_SHORT).show();

                                                                    // If the user pressed dequeue somewhere
                                                                    // in the app but wasn't logged in
                                                                    } else if (externalFragmentMessage.equals("dequeue")) {
                                                                        mViewModel.dequeueUser();
                                                                        Navigation.findNavController(v).
                                                                                navigate(R.id.queueFragment);
                                                                        Toast.makeText(getActivity(),
                                                                                "Removed from the queue!",
                                                                                Toast.LENGTH_SHORT).show();

                                                                    // If the user pressed see queue somewhere
                                                                    // in the app but wasn't logged in
                                                                    } else if (externalFragmentMessage.equals("seeQueue")) {
                                                                        Navigation.findNavController(v).
                                                                                navigate(R.id.queueFragment);
                                                                        Toast.makeText(getActivity(),
                                                                                "Welcome to the queue!",
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }

                                                                // If the user signed in without previously
                                                                // having pressed any button related to
                                                                // the queue anywhere else in the app
                                                                } else {
                                                                    Navigation.findNavController(v).
                                                                            navigate(R.id.userChoiceFragment);
                                                                }

                                                            // Make the user sign the waiver if they haven't
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

            mForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Sending reset password email.");
                    // Empty these fields before sending email.
                    mEmailText.setText("");
                    mPasswordText.setText("");

                    // Create AlertDialog to allow user to input their email.
                    final AlertDialog.Builder mPasswordResetAlert = new AlertDialog.Builder(getActivity());
                    mPasswordResetAlert.setTitle("Reset Password");
                    mPasswordResetAlert.setMessage("Please enter your email address below to reset " +
                            "your password.");

                    // EditText where user will provide email address.
                    final EditText input = new EditText(getActivity());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    mPasswordResetAlert.setView(input);

                    // Once user clicks the "Reset my password" button, an email will be sent with
                    // instructions.
                    mPasswordResetAlert.setPositiveButton("Reset my password",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.sendPasswordResetEmail(input.toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(getActivity(), "Reset password " +
                                                        "email has been sent! Please follow the " +
                                                        "instructions provided in that email.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });

                    // The "Cancel" button dismisses the AlertDialog.
                    mPasswordResetAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    mPasswordResetAlert.show();
                }
            });
        }
    }
}
