package org.nieghborhoodbikeworks.nbw.ui.orientation;

import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;

public class OrientationFragment extends Fragment {

    private SharedViewModel mViewModel;

    private Activity mContext;

    public static OrientationFragment newInstance() {
        return new OrientationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((DrawerLocker)getActivity()).setDrawerLocked(false);
        return inflater.inflate(R.layout.orientation_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        // TODO: Use the ViewModel


    }

}
