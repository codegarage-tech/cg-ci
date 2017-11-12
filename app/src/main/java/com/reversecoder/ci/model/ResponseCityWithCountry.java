package com.reversecoder.ci.model;

import java.util.ArrayList;

/**
 * Md. Rashadul Alam
 */
public class ResponseCityWithCountry extends ResponseBase {

    private String status = "";
    private ArrayList<CityWithCountry> data;

    public ResponseCityWithCountry(String status, ArrayList<CityWithCountry> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CityWithCountry> getData() {
        return data;
    }

    public void setData(ArrayList<CityWithCountry> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
