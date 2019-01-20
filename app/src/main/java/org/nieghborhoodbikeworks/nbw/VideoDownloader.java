package org.nieghborhoodbikeworks.nbw;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
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

public class VideoDownloader {
    private static String TAG = "Video Downloader";
    private Context context;
    private IVideoDownloadListener iVideoDownloadListener;
    private final int TIMEOUT_CONNECTION = 5000; // 5 sec
    private final int TIMEOUT_SOCKET = 30000; // 30 sec

    public VideoDownloader(Context context) {
        this.context = context;
    }

    /**
     * startVideosDownloading iterates through the list of videos (urls), asking on each iteration (i.e.
     * for each video) whether or not it has been downloaded. If it has not been downloaded, the
     * downloadVideo() method is called for that particular url.
     *
     * @param videosList The list of videos that will be displayed in the Orientation fragment
     */
    public void startVideosDownloading(final ArrayList<Video> videosList) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Iterate through the list of videos
                for(int i = 0; i < videosList.size(); i++) {
                    Log.i(TAG, "Thread is running");
                    Log.i(TAG, videosList.get(i).toString());
                    final Video video = videosList.get(i);
                    String url = video.getUrl();

                    // Check if the current video has been previously downloaded
                    String isVideoDownloaded = Utils.readPreferences(context, video.getUrl(), "false");
                    boolean isVideoAvailable = Boolean.valueOf(isVideoDownloaded);
                    Log.i(TAG, isVideoDownloaded);

                    // Download the video file if it has not been downloaded
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
     * @param urlStr Url from which mp4 data will be downloaded
     */
    private void downloadVideo(String urlStr, String videoId) {
        try {

            // Create "Video" folder in INTERNAL STORAGE; in the emulator, the internal storage is
            // titled "Android SDK Built for x00"
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            rootFile.mkdirs();

            URL url = null;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            long startTime = System.currentTimeMillis();
            Log.i(TAG, "Video download beginning: " + urlStr);

            // Open a connection to that URL
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            // This timeout affects how long it takes for the app to realize there's a connection problem
            c.setReadTimeout(TIMEOUT_CONNECTION);
            c.setConnectTimeout(TIMEOUT_SOCKET);

            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            // Define InputStreams to read from the URLConnection; uses 3KB download buffer
            InputStream is = c.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

            // Files created in the "Video" folder will be titled "Video (videoId).mp4"
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    "Video " + videoId + ".mp4"));

            byte[] buff = new byte[5 * 1024];
            //Read bytes (and store them) until there is nothing more to read(-1)
            int len;
            while ((len = inStream.read(buff)) != -1) {
                f.write(buff,0,len);
            }

            // Clean up
            f.flush();
            f.close();
            inStream.close();

            Log.i(TAG, "download completed in "
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");
        } catch (IOException e) {
            Log.d("Error....", e.toString());
        }

    }

    // TODO: Document this method
    public void setOnVideoDownloadListener(IVideoDownloadListener iVideoDownloadListener) {
        this.iVideoDownloadListener = iVideoDownloadListener;
    }
}