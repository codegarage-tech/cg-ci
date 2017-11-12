package com.reversecoder.ci.model;

import java.util.ArrayList;

/**
 * Md. Rashadul Alam
 */
public class CityWithCountry extends ResponseBase {

    private String id = "";
    private String name = "";
    private ArrayList<SpinnerItem> city;

    public CityWithCountry(String id, String name, ArrayList<SpinnerItem> city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SpinnerItem> getCity() {
        return city;
    }

    public void setCity(ArrayList<SpinnerItem> city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city=" + city +
                '}';
    }
}
