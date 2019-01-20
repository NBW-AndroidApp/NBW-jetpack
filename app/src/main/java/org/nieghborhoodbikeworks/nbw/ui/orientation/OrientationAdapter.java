package org.nieghborhoodbikeworks.nbw.ui.orientation;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.nieghborhoodbikeworks.nbw.R;
import org.nieghborhoodbikeworks.nbw.Utils;
import org.nieghborhoodbikeworks.nbw.Video;
import org.nieghborhoodbikeworks.nbw.VideoPlayer;
import org.nieghborhoodbikeworks.nbw.VideoPlayerController;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrientationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static String TAG = "Orientation Adapter";
    private Context context;
    private ArrayList mVideos;
    private LayoutInflater inflater;
    public VideoPlayerController videoPlayerController;

    /**
     * The adapter populates the data into the RecyclerView by converting an object at a position
     * into a list row item to be inserted. The adapter requires the existence of a "ViewHolder"
     * object which describes and provides access to all the views within each item row.
     *
     * @param context
     * @param mVideos The list of videos to be displayed on the Orientation fragment
     */
    public OrientationAdapter(Context context, final ArrayList mVideos) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mVideos = mVideos;
        videoPlayerController = new VideoPlayerController(context);
    }

    // Making the title of the fragment a ViewHolder item instead of a TextView allows for
    // continuous scrolling; using a TextView would result in a "sticky" header
    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TitleViewHolder(View view) {
            super(view);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ProgressBar progressBar;
        public RelativeLayout layout;

        public VideoViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.layout);
            textView = view.findViewById(R.id.textView);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the XML layout file that will be used for each row within the list
        View view = null;
        RecyclerView.ViewHolder vh = null;
//        if(viewType == 0) {
//            view = inflater.inflate(R.layout.orientation_title, parent, false);
//            vh = new TitleViewHolder(view);
//        } else {
            view = inflater.inflate(R.layout.orientation_fragment, parent, false);

            Configuration configuration = context.getResources().getConfiguration();
            int screenWidthDp = configuration.screenWidthDp; // The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.
            int smallestScreenWidthDp = configuration.smallestScreenWidthDp; // The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.

            vh = new VideoViewHolder(view);

            int screenWidthPixels = Utils.convertDpToPixel(screenWidthDp, context);
            RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, screenWidthPixels);

        ((VideoViewHolder)vh).layout.setLayoutParams(rel_btn);
        //}
        return vh;
    }

    /**
     * Sets the view attributes based on the data.
     *
     * @param holder The ViewHolder type: either TitleViewHolder or VideoViewHolder
     * @param position The video at position 'position'
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(mVideos.get(position).equals("Title")) {
            //do nothing, this is the title
        } else {
            // Initialize video item UI features
            Video video = ((Video)mVideos.get(position));
            ((VideoViewHolder)holder).textView.setText("Video " + video.getId());

            final VideoPlayer videoPlayer = new VideoPlayer(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            videoPlayer.setLayoutParams(params);

            ((VideoViewHolder)holder).layout.addView(videoPlayer);
            videoPlayerController.loadVideo(video, videoPlayer, ((VideoViewHolder)holder).progressBar);
            videoPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoPlayer.changePlayState();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if(mVideos.get(position).equals("Title")) {
            return 0;
//        } else {
//            return 1;
//        }
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

}