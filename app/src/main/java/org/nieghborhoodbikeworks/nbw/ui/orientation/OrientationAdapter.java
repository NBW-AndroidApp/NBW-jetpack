package org.nieghborhoodbikeworks.nbw.ui.orientation;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import org.nieghborhoodbikeworks.nbw.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrientationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater inflater;
    private final ArrayList<String> mVideos;

    // Making the title of the fragment a ViewHolder item instead of a TextView allows for
    // continuous scrolling; using a TextView would result in a "sticky" header
    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TitleViewHolder(View view) {
            super(view);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private VideoView mVideoPlayer;
        private View mView;
        private String VideoURL;
        private MediaController mMediaController;

        public VideoViewHolder(View view, String mURL) {
            super(view);
            mView = view;
            VideoURL = mURL;
            mVideoPlayer = mView.findViewById(R.id.videoView);
        }

        public void bindData() {
            mMediaController = new MediaController(mView.getContext());
            mVideoPlayer.setMediaController(mMediaController);
            mMediaController.setAnchorView(mVideoPlayer);
            mVideoPlayer.setKeepScreenOn(true);
            mVideoPlayer.setVideoURI(Uri.parse(VideoURL));
            mVideoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoPlayer.start();
                }
            });
        }
    }

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row. In our case,
     * each item row is composed of CardViews.
     *
     * @param context
     * @param mVideos
     */
    public OrientationAdapter(Context context, ArrayList<String> mVideos) {
        inflater = LayoutInflater.from(context);
        this.mVideos = mVideos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = null;
        RecyclerView.ViewHolder vh = null;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.orientation_title, parent, false);
                vh = new TitleViewHolder(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.video_item, parent, false);
                vh = new VideoViewHolder(view,"http://techslides.com/demos/sample-videos/small.mp4");
                break;
        }
        return vh;
    }

    /**
     * Sets the view attributes based on the data.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(position) {
            case 0:
                break;
            case 1:
                ((VideoViewHolder)holder).bindData();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mVideos.get(position).equals("Title")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

}
