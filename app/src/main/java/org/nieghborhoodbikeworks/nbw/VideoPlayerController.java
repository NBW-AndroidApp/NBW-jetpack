package org.nieghborhoodbikeworks.nbw;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class VideoPlayerController {
    private Context context;
    private FileCache fileCache;
    private int currentPositionOfItemToPlay = 0;
    private Video currentPlayingVideo;
    private Map<String, VideoPlayer> videos = Collections.synchronizedMap(new WeakHashMap<String, VideoPlayer>());
    private Map<String, ProgressBar> videosSpinner = Collections.synchronizedMap(new WeakHashMap<String, ProgressBar>());

    public VideoPlayerController(Context context) {
        this.context = context;
        fileCache = new FileCache(context);
    }

    public void loadVideo(Video video, VideoPlayer videoPlayer, ProgressBar progressBar) {
        // Add video to map
        videos.put(video.getIndexPosition(), videoPlayer);
        videosSpinner.put(video.getIndexPosition(), progressBar);
        handlePlayBack(video);
    }

    /**
     * This method would check two things: first if video is downloaded or its local path exists;
     * second if the videoplayer of this video is currently showing in the recyclerview or is visible
     *
     * @param video Video to be played
     */
    public void handlePlayBack(Video video) {
        // Check if video is available
        if(isVideoDownloaded(video)) {
            // Then check if it is currently at a visible or playable position in the recyclerview
            if(isVideoVisible(video)) {
                // If yes, then playvideo
                playVideo(video);
            }
        }
    }

    private void playVideo(final Video video) {
        // Before playing the video check if this video is already playing
        if(currentPlayingVideo != video) {
            // Start playing new url
            if(videos.containsKey(video.getIndexPosition())) {
                final VideoPlayer videoPlayer2 = videos.get(video.getIndexPosition());
                String localPath = fileCache.getFile(video.getUrl()).getAbsolutePath();
                if(!videoPlayer2.isLoaded) {
                    videoPlayer2.loadVideo(localPath, video);
                    videoPlayer2.setOnVideoPreparedListener(new IVideoPreparedListener() {
                        @Override
                        public void onVideoPrepared(Video mVideo) {
                            // Pause current playing video if any
                            if(video.getIndexPosition() == mVideo.getIndexPosition()) {
                                if(currentPlayingVideo!=null) {
                                    VideoPlayer videoPlayer1 = videos.get(currentPlayingVideo.getIndexPosition());
                                    videoPlayer1.pausePlay();
                                }
                                videoPlayer2.mp.start();
                                currentPlayingVideo = mVideo;
                            }
                        }
                    });
                } else {
                    // Pause current playing video if any
                    if(currentPlayingVideo!=null) {
                        VideoPlayer videoPlayer1 = videos.get(currentPlayingVideo.getIndexPosition());
                        videoPlayer1.pausePlay();
                    }
                    currentPlayingVideo = video;
                }
            }
        }
        else {
            Log.i(TAG, "Already playing Video: " + video.getUrl());
        }
    }

    private boolean isVideoVisible(Video video) {
        // To check if the video is visible in the recyclerview or it is currently at a playable position
        // we need the position of this video in recyclerview and the current scroll position of the recyclerview
        int positionOfVideo = Integer.valueOf(video.getIndexPosition());
        if(currentPositionOfItemToPlay == positionOfVideo) {
            return true;
        }
        return false;
    }

    private boolean isVideoDownloaded(Video video) {
        String isVideoDownloaded = Utils.readPreferences(context, video.getUrl(), "false");
        boolean isVideoAvailable = Boolean.valueOf(isVideoDownloaded);
        if(isVideoAvailable) {
            // If video is downloaded then hide its progress
            hideProgressSpinner(video);
            return true;
        }
        showProgressSpinner(video);
        return false;
    }

    private void showProgressSpinner(Video video) {
        ProgressBar progressBar = videosSpinner.get(video.getIndexPosition());
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressSpinner(Video video) {
        ProgressBar progressBar = videosSpinner.get(video.getIndexPosition());
        if(progressBar!=null && progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
            Log.i(TAG, "ProgressSpinner hidden at index: " + video.getIndexPosition());
        }
    }

    public void setcurrentPositionOfItemToPlay(int mCurrentPositionOfItemToPlay) {
        currentPositionOfItemToPlay = mCurrentPositionOfItemToPlay;
    }
}