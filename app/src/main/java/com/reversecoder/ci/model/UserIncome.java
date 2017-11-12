package com.reversecoder.ci.model;

/**
 * Md. Rashadul Alam
 */
public class UserIncome extends ResponseBase {

    private String total_income = "";
    private String first_name = "";
    private String last_name = "";

    public UserIncome(String total_income, String first_name, String last_name) {
        this.total_income = total_income;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getTotal_income() {
        return total_income;
    }

    public void setTotal_income(String total_income) {
        this.total_income = total_income;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "{" +
                "total_income='" + total_income + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
