package com.reversecoder.ci.model;

import java.util.ArrayList;

/**
 * Md. Rashadul Alam
 */
public class CityWithCountryData extends ResponseBase {

    private ArrayList<CityWithCountry> data;

    public CityWithCountryData(ArrayList<CityWithCountry> data) {
        this.data = data;
    }

    public ArrayList<CityWithCountry> getData() {
        return data;
    }

    public void setData(ArrayList<CityWithCountry> data) {
        this.data = data;
    }

    public ArrayList<SpinnerItem> getCountry() {
        ArrayList<SpinnerItem> mCountry = new ArrayList<SpinnerItem>();
        for (int i = 0; i < data.size(); i++) {
            mCountry.add(new SpinnerItem(data.get(i).getId(), data.get(i).getName()));
        }
        return mCountry;
    }

    public ArrayList<SpinnerItem> getCity(String countryName) {
        ArrayList<SpinnerItem> mCity = new ArrayList<SpinnerItem>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().contains(countryName)) {
                mCity = data.get(i).getCity();
                if (!isItemExist(mCity, "Choose Your Location")) {
                    mCity.add(0, new SpinnerItem("42343434343", "Choose Your Location"));
                }
                return mCity;
            }
        }
        return mCity;
    }

    private boolean isItemExist(ArrayList<SpinnerItem> data, String itemName) {
        boolean isExist = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equalsIgnoreCase(itemName)) {
                isExist = true;
                return isExist;
            }
        }
        return isExist;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}
