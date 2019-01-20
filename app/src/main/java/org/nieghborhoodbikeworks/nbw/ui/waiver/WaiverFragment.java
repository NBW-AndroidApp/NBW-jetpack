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
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.User;

public class WaiverFragment extends Fragment {
    private static final String TAG = "Waiver Fragment";
    private SharedViewModel mViewModel;
    private DatabaseReference mUserDatabase;
    private CheckBox mAgreementCheckBox;
    private EditText mSignature;
    private EditText mDate;
    private Button mSignWaiver;
    private User mUser;
    private View mView;

    public static WaiverFragment newInstance() {
        return new WaiverFragment();
    }

    /**
     * This initializes the UI variables once the fragment starts up, and returns the view
     * to its parent.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Waiver Fragment");
        ((DrawerLocker) getActivity()).setDrawerLocked(false);

        Log.d(TAG,"1");
        mView = inflater.inflate(R.layout.waiver_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        mUserDatabase = mViewModel.getUserDatabase();
        mAgreementCheckBox = mView.findViewById(R.id.agreement_checkbox);
        mSignature = mView.findViewById(R.id.waiver_signature);
        mDate = mView.findViewById(R.id.waiver_date);
        mSignWaiver = mView.findViewById(R.id.submit_waiver_button);
        mSignature.setEnabled(false);
        mDate.setEnabled(false);
        mSignWaiver.setEnabled(false);
        mUser = mViewModel.getUser();
        return mView;
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
                Navigation.findNavController(mView).navigate(R.id.queueFragment);
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