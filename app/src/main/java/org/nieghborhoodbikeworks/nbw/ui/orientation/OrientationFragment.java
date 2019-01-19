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
import org.nieghborhoodbikeworks.nbw.IVideoDownloadListener;
import org.nieghborhoodbikeworks.nbw.MainActivity;
import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.SharedViewModel;
import org.nieghborhoodbikeworks.nbw.Utils;
import org.nieghborhoodbikeworks.nbw.Video;
import org.nieghborhoodbikeworks.nbw.VideoDownloader;

import java.util.ArrayList;

public class OrientationFragment extends Fragment {
    private View mView;
    private SharedViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private OrientationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Video> mVideos;
    private VideoDownloader videoDownloader;

    public static OrientationFragment newInstance() {
        return new OrientationFragment();
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
        ((MainActivity) getActivity()).setActionBarTitle("Orientation Fragment");
        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.orientation_fragment, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        // Initialize orientation recyclerview UI elements
        mRecyclerView = mView.findViewById(R.id.video_recycler_view);
        progressBar = mView.findViewById(R.id.progressBar);
        mVideos = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new OrientationAdapter(getActivity(), mVideos);
        mRecyclerView.setAdapter(mAdapter);

        videoDownloader = new VideoDownloader(getActivity());
        videoDownloader.setOnVideoDownloadListener(new IVideoDownloadListener() {
            @Override
            public void onVideoDownloaded(Video video) {
                mAdapter.videoPlayerController.handlePlayBack(video);
            }
        });

        // Start downloading videos if there is a reliable internet connection
        if(Utils.isNetworkAvailable(mView.getContext())) {
            getVideos();
        } else {
            Toast.makeText(getActivity(), "No internet connection available", Toast.LENGTH_LONG).show();
        }
        return mView;
    }

    /**
     * Adds the video urls to the mVideos ArrayList and calls the startVideosDownloading() method.
     *
     */
    private void getVideos() {
        Video video1 = new Video("0", "1", "http://techslides.com/demos/sample-videos/small.mp4");
        mVideos.add(video1);
        Video video2 = new Video("1", "2", "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
        mVideos.add(video2);
        Video video3 = new Video("2", "3", "http://sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4");
        mVideos.add(video3);
        Video video4 = new Video("3", "4", "http://dev.exiv2.org/attachments/341/video-2012-07-05-02-29-27.mp4");
        mVideos.add(video4);
        Video video5 = new Video("4", "5", "http://techslides.com/demos/sample-videos/small.mp4");
        mVideos.add(video5);
        Video video6 = new Video("5", "6", "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4");
        mVideos.add(video6);
        Video video7 = new Video("6", "7", "http://sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4");
        mVideos.add(video7);

        mAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        videoDownloader.startVideosDownloading(mVideos);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}