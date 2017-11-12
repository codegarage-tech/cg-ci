package com.reversecoder.ci.model;

import java.util.ArrayList;

/**
 * Md. Rashadul Alam
 */
public class ResponseUserData extends ResponseBase {

    private String status = "";
    private String msg = "";
    private ArrayList<UserData> user_data = new ArrayList<UserData>();

    public ResponseUserData() {
    }

    public ResponseUserData(String status, String msg, ArrayList<UserData> user_data) {
        this.status = status;
        this.msg = msg;
        this.user_data = user_data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<UserData> getUser_data() {
        return user_data;
    }

    public void setUser_data(ArrayList<UserData> user_data) {
        this.user_data = user_data;
    }
}
