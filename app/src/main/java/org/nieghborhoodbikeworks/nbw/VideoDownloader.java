package org.nieghborhoodbikeworks.nbw;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class VideoDownloader {
    private Context context;
    private IVideoDownloadListener iVideoDownloadListener;

    public VideoDownloader(Context context) {
        this.context = context;
    }

    /**
     * startVideosDownloading iterates through the list of videos (urls), asking on each iteration (i.e.
     * for each video) whether or not it has been downloaded. If it has not been downloaded, the
     * downloadVideo() method is called for that particular url.
     *
     * @param videosList
     */
    public void startVideosDownloading(final ArrayList<Video> videosList) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < videosList.size(); i++) {
                    Log.i(TAG, "Thread is running");
                    Log.i(TAG, videosList.get(i).toString());
                    final Video video = videosList.get(i);
                    String url = video.getUrl();

                    String isVideoDownloaded = Utils.readPreferences(context, video.getUrl(), "false");
                    boolean isVideoAvailable = Boolean.valueOf(isVideoDownloaded);
                    Log.i(TAG, isVideoDownloaded);
                    if(!isVideoAvailable) {
                        Log.i(TAG, "Downloading video");
                        // Download video from url
                        downloadVideo(url, video.getId());
                        Activity activity = (Activity) context;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.savePreferences(context, video.getUrl(), "true");
                                iVideoDownloadListener.onVideoDownloaded(video);
                            }
                        });
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * Downloads a video from a url to the device's local storage.
     *
     * @param urlStr
     */
    private void downloadVideo(String urlStr, String videoId) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video" + File.separator + videoId + ".mp4";
            File rootFile = new File(rootDir);
            rootFile.mkdir();

            Log.i(TAG, rootFile.toString());

            URL url = new URL(urlStr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    "Video " + videoId + ".mp4"));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }

    }

    public void setOnVideoDownloadListener(IVideoDownloadListener iVideoDownloadListener) {
        this.iVideoDownloadListener = iVideoDownloadListener;
    }
}