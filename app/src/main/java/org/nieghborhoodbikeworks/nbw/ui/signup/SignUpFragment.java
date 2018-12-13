package org.nieghborhoodbikeworks.nbw.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        // Set views for listening events
        return v;
    }



}
