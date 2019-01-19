package org.nieghborhoodbikeworks.nbw;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class VideoDownloader {
    private Context context;
    private FileCache fileCache;
    private IVideoDownloadListener iVideoDownloadListener;

    public VideoDownloader(Context context) {
        this.context = context;
        fileCache = new FileCache(context);
    }

    /**
     * startVideosDownloading iterates through the list of videos (urls), asking on each iteration (i.e.
     * for each video) whether or not it has been downloaded.
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
                        downloadVideo(url);
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

    private String downloadVideo(String urlStr) {
        URL url = null;
        File file = null;
        try {
            file = fileCache.getFile(urlStr);
            url = new URL(urlStr);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[5 * 1024];

            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
            }

            //clean up
            outStream.flush();
            outStream.close();
            inStream.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void setOnVideoDownloadListener(IVideoDownloadListener iVideoDownloadListener) {
        this.iVideoDownloadListener = iVideoDownloadListener;
    }
}