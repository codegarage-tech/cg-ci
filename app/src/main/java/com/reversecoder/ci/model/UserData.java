package com.reversecoder.ci.model;

/**
 * Md. Rashadul Alam
 */
public class UserData extends ResponseBase {

    private String id = "";
    private String first_name = "";
    private String last_name = "";
    private String email = "";
    private String password = "";
    private String gender = "";
    private String modified_at = "";
    private String ip = "";
    private String city = "";
    private String country = "";
    private String date_of_birth = "";

    public UserData(String id, String first_name, String last_name, String email, String password, String gender, String modified_at, String ip, String city, String country, String date_of_birth) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.modified_at = modified_at;
        this.ip = ip;
        this.city = city;
        this.country = country;
        this.date_of_birth = date_of_birth;
    }

    public UserData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", modified_at='" + modified_at + '\'' +
                ", ip='" + ip + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                '}';
    }
}
