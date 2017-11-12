package com.reversecoder.ci.util;

import android.util.Log;

import org.json.JSONObject;

/**
 * Md. Rashadul Alam
 */
public class AllUrls {

    private static String TAG = AllUrls.class.getSimpleName();
    private static final String BASE_URL = "http://communiverseintl.com/apis/index.php/";
    public static final String APP_WEB_URL = "http://communiverseintl.com/";

    public static String getLoginUrl() {
        String url = BASE_URL + "login";
        Log.d(TAG, "getLoginUrl: " + url);
        return url;
    }

    public static JSONObject getLoginParameters(String email, String password) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .getJSONParam();
        Log.d(TAG, "getLoginParameters: " + params.toString());
        return params;
    }

    public static String getSignUpUrl() {
        String url = BASE_URL + "signup";
        Log.d(TAG, "getSignUpUrl: " + url);
        return url;
    }

    public static JSONObject getSignUpParameters(String email, String password, String firstName, String lastName, String gender) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .addJSONParam("first_name", firstName)
                .addJSONParam("last_name", lastName)
                .addJSONParam("gender", gender)
                .getJSONParam();
        Log.d(TAG, "getSignUpParameters: " + params.toString());
        return params;
    }

    public static String getUserDetailUrl(String userId) {
        String url = BASE_URL + "user/details/" + userId;
        Log.d(TAG, "getUserDetailUrl: " + url);
        return url;
    }

    public static String getUpdateUserUrl() {
        String url = BASE_URL + "user/update";
        Log.d(TAG, "getUpdateUserUrl: " + url);
        return url;
    }

    public static JSONObject getUpdateUserParameters(String id, String email, String password, String firstName, String lastName, String gender, String city, String country, String dateOfBirth) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("id", id)
                .addJSONParam("email", email)
                .addJSONParam("password", password)
                .addJSONParam("first_name", firstName)
                .addJSONParam("last_name", lastName)
                .addJSONParam("gender", gender)
                .addJSONParam("city", city)
                .addJSONParam("country", country)
                .addJSONParam("date_of_birth", dateOfBirth)
                .getJSONParam();
        Log.d(TAG, "getUpdateUserParameters: " + params.toString());
        return params;
    }

    public static String getAllCountriesUrl() {
        String url = BASE_URL + "location/all_countries";
        Log.d(TAG, "getAllCountriesUrl: " + url);
        return url;
    }

    public static String getAllStatesUrl() {
        String url = BASE_URL + "location/get_states";
        Log.d(TAG, "getAllStatesUrl: " + url);
        return url;
    }

    public static JSONObject getAllStatesParameters(String countryId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("country_id", countryId)
                .getJSONParam();
        Log.d(TAG, "getAllStatesParameters: " + params.toString());
        return params;
    }

    public static String getAllCitiesUrl() {
        String url = BASE_URL + "location/get_states";
        Log.d(TAG, "getAllCitiesUrl: " + url);
        return url;
    }

    public static JSONObject getAllCitiesParameters(String stateId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("state_id", stateId)
                .getJSONParam();
        Log.d(TAG, "getAllCitiesParameters: " + params.toString());
        return params;
    }

    public static String getAllAdvertisePlanesUrl() {
        String url = BASE_URL + "location/all_planes";
        Log.d(TAG, "getAllAdvertisePlanesUrl: " + url);
        return url;
    }

    public static JSONObject getAllAdvertisementPlanesParameters(String stateId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("state_id", stateId)
                .getJSONParam();
        Log.d(TAG, "getAllAdvertisementPlanesParameters: " + params.toString());
        return params;
    }

    public static String getAddAdvertisementUrl() {
        String url = BASE_URL + "advertisement/add";
        Log.d(TAG, "getAddAdvertisementUrl: " + url);
        return url;
    }

//    public static JSONObject getAddAdvertisementParameters(String stateId,String userId, String countryId, String planId, String gender, String age, String cityId) {
//        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
//                .addJSONParam("state_id", stateId)
//                .getJSONParam();
//        Log.d(TAG, "getSignUpParameters: " + params.toString());
//        return params;
//    }
//
////    {“state_id”:1, “user_id”: 1, “country_id”:1, “plan_id”:2, “gender”:”both”, “age”: 23-30,
//// “City_id”:[1,2,3],  ‘image’:’base64-encoded-image’ }

    public static String getUserIncomeUrl(String userId) {
        String url = BASE_URL + "adds/get_user_income/" + userId;
        Log.d(TAG, "getUserIncomeUrl: " + url);
        return url;
    }

    public static String getShowAdvertisementUrl() {
        String url = BASE_URL + "adds/show";
        Log.d(TAG, "showAdvertisementUrl: " + url);
        return url;
    }

    public static JSONObject getShowAdvertisementParameters(String userId, String sendSmsTo) {
        JSONObject params;
        if (!AppUtils.isNullOrEmpty(sendSmsTo)) {
            params = HttpRequestManager.HttpParameter.getInstance()
                    .addJSONParam("user_id", userId)
                    .addJSONParam("send_sms_to", sendSmsTo)
                    .getJSONParam();
        } else {
            params = HttpRequestManager.HttpParameter.getInstance()
                    .addJSONParam("user_id", userId)
                    .getJSONParam();
        }

        Log.d(TAG, "getShowAdvertisementParameters: " + params.toString());
        return params;
    }

    public static String getUserAdvertiseCountUrl() {
        String url = BASE_URL + "adds/user_add_count";
        Log.d(TAG, "getUserAdvertiseCountUrl: " + url);
        return url;
    }

    public static JSONObject getUserAdvertiseCountParameters(String userId, String advertiseId) {
        JSONObject params = HttpRequestManager.HttpParameter.getInstance()
                .addJSONParam("user_id", userId)
                .addJSONParam("add_id", advertiseId)
                .getJSONParam();
        Log.d(TAG, "getUserAdvertiseCountParameters: " + params.toString());
        return params;
    }

    public static String getAllCityWithCountryUrl() {
        String url = BASE_URL + "location/get_all_cities_list";
        Log.d(TAG, "getAllCityWithCountryUrl: " + url);
        return url;
    }
}
