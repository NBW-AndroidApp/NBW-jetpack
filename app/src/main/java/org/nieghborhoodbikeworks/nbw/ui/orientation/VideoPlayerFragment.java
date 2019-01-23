package org.nieghborhoodbikeworks.nbw.ui.orientation;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import org.nieghborhoodbikeworks.nbw.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class VideoPlayerFragment extends Fragment {
    private static String TAG = "VideoPlayerFragment";
    private View mView;
    private ProgressDialog mDialog;
    private VideoView videoView;
    private String videoURL;
    private Uri uri;

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
        // Get the view from fragment XML
        mView = inflater.inflate(R.layout.video_player_fragment, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // Set videoURL equal to the url passed from the adapter
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