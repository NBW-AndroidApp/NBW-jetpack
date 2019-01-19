package org.nieghborhoodbikeworks.nbw;

public class Video {
    private String mID;
    private String mIndexPosition;
    private String mURL;

    public Video(String ID, String indexPosition, String URL) {
        mID = ID;
        mIndexPosition = indexPosition;
        mURL = URL;
    }

    public String getId() {
        return mID;
    }

    public String getIndexPosition() {
        return mIndexPosition;
    }

    public String getUrl() {
        return mURL;
    }
}
