package org.nieghborhoodbikeworks.nbw;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

public final class Utils {

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static int convertDpToPixel(int screenWidthDp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (screenWidthDp * scale + 0.5f);
    }

    /**
     * Read from sharedPreferences
     *
     * @param context
     * @param url Key
     * @param defaultValue Value to return if key is not present (i.e. if key == null)
     * @return Whether or not the video has been downloaded
     */
    public static String readPreferences(Context context, String url, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String isVideoDownloaded = sharedPref.getString(url, defaultValue);
        return isVideoDownloaded;
    }

    /**
     * Write to sharedPreferences
     *
     * @param context
     * @param url Key
     * @param bool Value
     */
    public static void savePreferences(Context context, String url, String bool) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(url, bool);
        editor.apply();
    }
}