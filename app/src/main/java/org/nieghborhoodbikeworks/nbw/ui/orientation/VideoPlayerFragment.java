package org.nieghborhoodbikeworks.nbw.ui.orientation;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import org.nieghborhoodbikeworks.nbw.DrawerLocker;
import org.nieghborhoodbikeworks.nbw.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class VideoPlayerFragment extends Fragment {
    private static String TAG = "Video Player Fragment";
    private View mView;
    private ProgressDialog mDialog;
    private VideoView videoView;
    private String videoURL;
    private Uri uri;
    private Integer position;

    public static VideoPlayerFragment newInstance() {
        return new VideoPlayerFragment();
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
        // Hide action bar when displaying video; navigation drawer will still be accessible via swipe
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((DrawerLocker) getActivity()).setDrawerLocked(false);
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.video_player_fragment, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // Set videoURL equal to the url passed from the adapter; if there is no url present,
            // the defaultValue will be returned, which in our case is an empty string
            videoURL = bundle.getString("VideoURL");
        }

        // Initialize video UI elements
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Buffering...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        videoView = mView.findViewById(R.id.videoView);

        try {
            if (!videoView.isPlaying()) {
                uri = Uri.parse(videoURL);
                videoView.setVideoURI(uri);
            } else {
                videoView.pause();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mDialog.dismiss();
//                try {
//                    mp.prepare();
//                    mp.setDataSource(videoURL);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mp.setLooping(true);
                videoView.start();
            }
        });

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}