package com.example.android.newsapp;

import android.graphics.Bitmap;

public class Publication {


    private String title;
    private String section;
    private String publicationDate;
    private String url;
    private String trailText;
    private String thumbnail;
    private Bitmap thumbnailImage;


    public Publication(String title, String section, String publicationDate, String url, String trailText, String thumbnail, Bitmap thumbnailImage) {
        this.title = title;
        this.section = section;
        this.publicationDate = publicationDate;
        this.url = url;
        this.trailText = trailText;
        this.thumbnail = thumbnail;
        this.thumbnailImage = thumbnailImage;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getTrailText() {
        return trailText;
    }

    public String getUrl() {
        return url;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Bitmap getThumbnailImage() {
        return thumbnailImage;
    }
}
