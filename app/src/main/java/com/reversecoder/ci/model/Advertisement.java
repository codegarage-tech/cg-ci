package com.reversecoder.ci.model;

/**
 * Md. Rashadul Alam
 */
public class Advertisement extends ResponseBase {

    private String image = "";
    private String link = "";
    private String id = "";
    private String status = "";

    public Advertisement(String image, String link, String id, String status) {
        this.image = image;
        this.link = link;
        this.id = id;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
