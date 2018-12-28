package com.mobiotic.videoplay.demo.model;

public class VideoSQLiteData {

    int videoId;
    long seekPosition;


    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videId) {
        this.videoId = videId;
    }

    public long getSeekPosition() {
        return seekPosition;
    }

    public void setSeekPosition(long seekPosition) {
        this.seekPosition = seekPosition;
    }
}
