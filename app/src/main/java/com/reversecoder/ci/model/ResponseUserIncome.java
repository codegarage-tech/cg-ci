package com.reversecoder.ci.model;

import java.util.ArrayList;

/**
 * Md. Rashadul Alam
 */
public class ResponseUserIncome extends ResponseBase {

    private String status = "";
    private ArrayList<UserIncome> data = new ArrayList<UserIncome>();

    public ResponseUserIncome() {
    }

    public ResponseUserIncome(String status, ArrayList<UserIncome> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<UserIncome> getData() {
        return data;
    }

    public void setData(ArrayList<UserIncome> data) {
        this.data = data;
    }
}
