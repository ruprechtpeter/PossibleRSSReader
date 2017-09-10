package com.possible.rssreader.model;

/**
 * Created by Rupi on 2017. 09. 10..
 */

public class RssFeedModel {
    private String title;
    private String description;
    private String link;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public void setAll(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
