package org.nieghborhoodbikeworks.nbw.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.ui.login.LoginFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SignUpFragment extends Fragment {
    private String TAG = "SignUpFragment";

    public SignUpFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        ((MainActivity) getActivity()).setActionBarTitle("Sign Up with NBW");
        // Get the view from fragment XML
        Log.d(TAG, "HERE!");
        View v = inflater.inflate(R.layout.signup_fragment, container, false);

        Button signupButton = v.findViewById(R.id.signup_button);
        final EditText emailEditText = v.findViewById(R.id.email);
        final EditText passwordEditText = v.findViewById(R.id.password);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                FragmentManager fm = getFragmentManager();
                LoginFragment loginFrag = (LoginFragment)fm.findFragmentById(R.id.loginFragment);
                loginFrag.createAccount(email, password);
            }
        });

        // Set views for listening events
        return v;
    }

}
