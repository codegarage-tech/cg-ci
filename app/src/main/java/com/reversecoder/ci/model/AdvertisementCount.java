package com.reversecoder.ci.model;

/**
 * Md. Rashadul Alam
 */
public class AdvertisementCount extends ResponseBase {

    private String status = "";

    public AdvertisementCount(String status) {
        this.status = status;
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
                "status='" + status + '\'' +
                '}';
    }
}
