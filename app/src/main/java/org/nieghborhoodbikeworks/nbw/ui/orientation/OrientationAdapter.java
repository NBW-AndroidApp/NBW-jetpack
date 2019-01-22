package org.nieghborhoodbikeworks.nbw.ui.orientation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.nieghborhoodbikeworks.nbw.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class OrientationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static String TAG = "Orientation Adapter";
    private LayoutInflater inflater;
    private ArrayList<String> mVideos;
    private String url;

    /**
     * There are two types of ViewHolders, the first being the title of the Orientation fragment,
     * and the second being the CardViews containing thumbnails for the videos to be displayed.
     */

    // Making the title of the fragment a ViewHolder item instead of a TextView allows for
    // continuous scrolling of the CardViews and the title; using a TextView would result in a
    // "sticky" header
    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TitleViewHolder(View view) {
            super(view);
        }
    }

    // Video CardViews
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private Context mContext;
        private ImageView mThumbnail;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mLength;

        public VideoViewHolder(View view) {
            super(view);
            mView = view;
            mContext = mView.getContext();
            mThumbnail = mView.findViewById(R.id.orientation_video_thumbnail);
            mTitle = mView.findViewById(R.id.orientation_video_title);
            mDescription = mView.findViewById(R.id.orientation_video_description);
            mLength = mView.findViewById(R.id.orientation_video_length);
        }

        public void bindData() {
            mThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Play video
                    VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                    Bundle args = new Bundle();
                    args.putString("VideoURL", "http://techslides.com/demos/sample-videos/small.mp4");
                    videoPlayerFragment.setArguments(args);
                    Navigation.findNavController(mView).navigate(R.id.videoPlayerFragment);
                }
            });
            mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Play video
                    VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                    Bundle args = new Bundle();
                    args.putString("VideoURL", "http://techslides.com/demos/sample-videos/small.mp4");
                    videoPlayerFragment.setArguments(args);
                    Navigation.findNavController(mView).navigate(R.id.videoPlayerFragment);
                }
            });
            mDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Play video
                    VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                    Bundle args = new Bundle();
                    args.putString("VideoURL", "http://techslides.com/demos/sample-videos/small.mp4");
                    videoPlayerFragment.setArguments(args);
                    Navigation.findNavController(mView).navigate(R.id.videoPlayerFragment);
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
     * @param mVideos The videos that will be displayed as CardViews
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
                view = inflater.inflate(R.layout.orientation_video, parent, false);
                vh = new VideoViewHolder(view);
                break;
        }
        return vh;
    }

    /**
     * Sets the view attributes based on the data.
     *
     * @param holder
     * @param position The video at position 'position'
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                ((VideoViewHolder)holder).bindData();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int result = 0;
        if(mVideos.get(position).equals("Title")) {
            result = 0;
        } else {
            result = 1;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

}