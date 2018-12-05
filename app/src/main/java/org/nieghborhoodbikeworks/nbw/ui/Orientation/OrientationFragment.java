package org.nieghborhoodbikeworks.nbw.ui.Orientation;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.ui.waiver.WaiverFragment;
import org.nieghborhoodbikeworks.nbw.ui.waiver.WaiverViewModel;

public class OrientationFragment extends Fragment {

    private OrientationViewModel mViewModel;

    private Activity mContext;

    public static OrientationFragment newInstance() {
        return new OrientationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orientation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrientationViewModel.class);
        // TODO: Use the ViewModel


    }

}
