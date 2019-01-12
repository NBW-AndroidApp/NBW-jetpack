package org.nieghborhoodbikeworks.nbw.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class SignUpFragment extends Fragment {
    private View mView;
    private String TAG = "SignUpFragment";
    private SharedViewModel mViewModel;
    private Button mSignUpButton;
    private EditText mEmail, mPassword, mPasswordVerify, mName;
    private CheckBox mAgeCheckBox;

    public SignUpFragment(){}

    /**
     * Initializes the Sign Up Fragment and loads its visual elements into member variables.
     * mViewModel is set as the SharedViewModel used by MainActivity. This way, data is available
     * between fragments since SharedViewModel manages data for MainActivity.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Set Title for the Fragment
        ((MainActivity) getActivity()).setActionBarTitle("Sign Up with NBW");
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.signup_fragment, container, false);

        ((DrawerLocker)getActivity()).setDrawerLocked(false);

        // Fetch SharedViewModel from MainActivity.
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Assign values to the variables involved on the Fragment view.
        mSignUpButton= mView.findViewById(R.id.signup_button);
        mEmail = mView.findViewById(R.id.email);
        mPassword = mView.findViewById(R.id.password);
        mAgeCheckBox = mView.findViewById(R.id.age_checkbox);
        mPasswordVerify = mView.findViewById(R.id.password_verify);
        mName = mView.findViewById(R.id.name);

        // Set views for listening events
        return mView;
    }

    /**
     * This is the functional portion of the SignUpFragment.
     * It contains two onClickListeners, on mSignUpButton and mAgeCheckBox.
     *
     * When the user presses the mSignUpButton: E-mail, password, passwordVerify
     * and name are checked for validity. If any of them is invalid, then a Toast is shown to the user
     * to enter valid credentials. If all of them are valid, the info is taken in and a {@link FirebaseUser}
     * FirebaseUser is created as well as a {@link com.google.firebase.database.FirebaseDatabase}
     * FirebaseDataBase User instance. After that, the person is navigated into the UserChoiceFragment.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Sign up initiated");
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String name = mName.getText().toString();
                String passwordVerify = mPasswordVerify.getText().toString();
                if (!(isValidEmail(email) && (password.equals(passwordVerify)) && isValidPassword(password))) {
                    Toast.makeText(getActivity(), "Please enter valid email and passwords.",
                            Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                    mPasswordVerify.setText("");
                }
                else {
                    Log.d(TAG, "verifying");
                    final User user = new User(name, email);
                    mViewModel.getAuth().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(getActivity(),
                                                String.format("Hello %s! Signing you in.", user.getName()),
                                                Toast.LENGTH_SHORT).show();
                                        user.setUid(mViewModel.getAuth().getUid());
                                        mViewModel.createDatabaseUser(user);
                                        Navigation.findNavController(v).navigate(R.id.waiverFragment);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        mSignUpButton.setEnabled(false);
        mAgeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    mSignUpButton.setEnabled(true);
                } else {
                    mSignUpButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * Checks if a given password is valid. this is in place so it can be modified to have stricter
     * rules in the future.
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    /**
     * Checks e-mail validity using Android's {@link android.util.Patterns}.
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
