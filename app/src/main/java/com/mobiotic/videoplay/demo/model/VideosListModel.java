package com.mobiotic.videoplay.demo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideosListModel implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("thumb")
    private String thumb;
    @SerializedName("url")
    private String url;
    @SerializedName("description")
    private String description;

    public VideosListModel() {

    }


    public VideosListModel(String id, String title, String thumb, String url, String description) {
        this.id = id;
        this.title = title;
        this.thumb = thumb;
        this.url = url;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}