package org.nieghborhoodbikeworks.nbw.ui.signup;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.ui.login.LoginFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class SignUpFragment extends Fragment {
    private String TAG = "SignUpFragment";
    private SharedViewModel mViewModel;

    public SignUpFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        ((MainActivity) getActivity()).setActionBarTitle("Sign Up with NBW");
        // Get the view from fragment XML
        View v = inflater.inflate(R.layout.signup_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        Button signupButton = v.findViewById(R.id.signup_button);
        final EditText emailEditText = v.findViewById(R.id.email);
        final EditText passwordEditText = v.findViewById(R.id.password);
        final EditText passwordVerifyEditText = v.findViewById(R.id.password_verify);
        final EditText nameEditText = v.findViewById(R.id.name);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "here!");
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String passwordVerify = passwordVerifyEditText.getText().toString();
                if (!(isValidEmail(email) && (password.equals(passwordVerify)))) {
                    Toast.makeText(getActivity(), "BITCH you think I'm dumb?",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "verifying");
                    mViewModel.getAuth().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mViewModel.getAuth().getCurrentUser();
                                        mViewModel.checkDatabaseUser(user);
                                        if (!mViewModel.checkWaiverStatus(user)) {
                                            Navigation.findNavController(v).navigate(R.id.waiverFragment);
                                        }
                                        else {
                                            Navigation.findNavController(v).navigate(R.id.queueFragment);
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        // Set views for listening events
        return v;
    }

    public final static boolean isValidEmail(CharSequence target) {
        Log.d("SignUp", String.valueOf((!TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches())));
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
