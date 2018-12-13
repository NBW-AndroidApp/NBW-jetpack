package org.nieghborhoodbikeworks.nbw.ui.waiver;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import org.nieghborhoodbikeworks.nbw.R;

public class WaiverFragment extends Fragment {

    private WaiverViewModel mViewModel;

    public static WaiverFragment newInstance() {
        return new WaiverFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waiver_fragment, container, false);
        final CheckBox agreementCheckBox = view.findViewById(R.id.agreement_checkbox);
        final EditText signature = view.findViewById(R.id.waiver_signature);
        final EditText date = view.findViewById(R.id.waiver_date);
        signature.setEnabled(false);
        date.setEnabled(false);
        agreementCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    signature.setEnabled(true);
                    date.setEnabled(true);
                } else {
                    signature.setEnabled(false);
                    date.setEnabled(false);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WaiverViewModel.class);
        // TODO: Use the ViewModel
    }

}
