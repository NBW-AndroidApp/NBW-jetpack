package org.nieghborhoodbikeworks.nbw.ui.orientation;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;

import java.util.ArrayList;

public class OrientationFragment extends Fragment {
    private static String TAG = "Orientation Fragment";
    private View mView;
    private SharedViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private OrientationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mVideos;

    public static OrientationFragment newInstance() {
        return new OrientationFragment();
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
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Orientation Fragment");
        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.orientation_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Initialize orientation recyclerview UI elements
        mRecyclerView = mView.findViewById(R.id.orientation_recycler_view);
        mVideos = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mVideos.add("Title");

        // Add video urls to mVideos array
        mVideos.add("http://techslides.com/demos/sample-videos/small.mp4");
        mVideos.add("http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
        mVideos.add("http://sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4");
        mVideos.add("http://dev.exiv2.org/attachments/341/video-2012-07-05-02-29-27.mp4");
        mVideos.add("http://techslides.com/demos/sample-videos/small.mp4");
        mVideos.add("http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
        mVideos.add("http://sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4");

        mAdapter = new OrientationAdapter(getActivity(), mVideos);
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}