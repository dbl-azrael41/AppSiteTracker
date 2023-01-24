package com.example.apptracker_v1;

public class Activity {
    private int id;
    private boolean isFavorite;
    private String url;
    private boolean isActive;

    public Activity() {}
    public Activity(boolean isFavorite, String url, boolean isActive) {
        this.isFavorite = isFavorite;
        this.url = url;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
