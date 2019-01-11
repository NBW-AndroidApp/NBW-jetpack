package org.nieghborhoodbikeworks.nbw.ui.waiver;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

public class WaiverFragment extends Fragment {

    private static final String TAG = "WaiverFrag onCreateView";
    private SharedViewModel mViewModel;
    private DatabaseReference mUserDatabase;
    private CheckBox mAgreementCheckBox;
    private EditText mSignature;
    private EditText mDate;
    private Button mSignWaiver;
    private User mUser;
    private View view;

    public static WaiverFragment newInstance() {
        return new WaiverFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ((DrawerLocker)getActivity()).setDrawerLocked(false);

        Log.d(TAG,"1");
        view = inflater.inflate(R.layout.waiver_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        mUserDatabase = mViewModel.getUserDatabase();
        mAgreementCheckBox = view.findViewById(R.id.agreement_checkbox);
        mSignature = view.findViewById(R.id.waiver_signature);
        mDate = view.findViewById(R.id.waiver_date);
        mSignWaiver = view.findViewById(R.id.submit_waiver_button);
        mSignature.setEnabled(false);
        mDate.setEnabled(false);
        mSignWaiver.setEnabled(false);
        mUser = mViewModel.getUser();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAgreementCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    mSignature.setEnabled(true);
                    mDate.setEnabled(true);
                } else {
                    mSignature.setEnabled(false);
                    mDate.setEnabled(false);
                }
            }
        });

        mSignature.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredFields();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkRequiredFields();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        mSignWaiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), mViewModel.getUser().getName(),
                        Toast.LENGTH_SHORT).show();
                String userID = mUser.getUid();
                mUserDatabase.child(userID).child("signedWaiver").setValue(true);
                Navigation.findNavController(view).navigate(R.id.queueFragment);
            }
        });

    }

    private void checkRequiredFields() {
        if (!mSignature.getText().toString().isEmpty() && !mDate.getText().toString().isEmpty()) {
            mSignWaiver.setEnabled(true);
        } else {
            mSignWaiver.setEnabled(false);
        }
    }

}