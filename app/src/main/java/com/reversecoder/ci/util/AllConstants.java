package com.reversecoder.ci.util;

import com.reversecoder.ci.model.SpinnerItem;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AllConstants {

    //Session key
    public static final String SESSION_IS_USER_LOGGED_IN = "SESSION_IS_USER_LOGGED_IN";
    public static final String SESSION_USER_DATA = "SESSION_USER_DATA";
    public static final String SESSION_USER_INCOME = "SESSION_USER_INCOME";
    public static final String SESSION_CITY_WITH_COUNTRY = "SESSION_CITY_WITH_COUNTRY";

    //Intent key
    public static String KEY_INTENT_STATUS = "KEY_INTENT_STATUS";
    public static String KEY_INTENT_MESSAGE = "KEY_INTENT_MESSAGE";
    public static String KEY_PARCELABLE_CONTACT = "KEY_PARCELABLE_CONTACT";
    public static String KEY_CONTACT_DETAIL = "KEY_CONTACT_DETAIL";

    // Reside menu
    public static final String TITLE_FRAGMENT_CONTACTS = "Contacts";
    public static final String TITLE_FRAGMENT_PROFILE = "Profile";
    public static final String TITLE_MENU_LOGOUT = "Logout";
    public static final String TITLE_FRAGMENT_TERMS_AND_CONDITIONS = "Terms and Conditions";
    public static final String TITLE_FRAGMENT_PRIVACY_AND_POLICY = "Privacy and Policy";
    public static final String TITLE_FRAGMENT_HOW_IT_WORKS = "How it works";

    public static final String TAG_FRAGMENT_CONTACT = "TAG_FRAGMENT_CONTACT";
    public static final String TAG_FRAGMENT_PROFILE = "TAG_FRAGMENT_PROFILE";
    public static final String TAG_FRAGMENT_HOW_IT_WORKS = "TAG_FRAGMENT_HOW_IT_WORKS";
    public static final String TAG_FRAGMENT_TERMS_AND_CONDITIONS = "TAG_FRAGMENT_TERMS_AND_CONDITIONS";
    public static final String TAG_FRAGMENT_PRIVACY_AND_POLICY = "TAG_FRAGMENT_PRIVACY_AND_POLICY";
    public static final String TAG_FRAGMENT_DATE_PICKER = "TAG_FRAGMENT_DATE_PICKER";

    //Asset files
    public static final String ASSET_FILE_PRIVACY_AND_POLICY = "privacy_policy.pdf";
    public static final String ASSET_FILE_TERMS_AND_CONDITIONS = "terms_and_conditions.pdf";


    public static ArrayList<SpinnerItem> getGenderData() {

        ArrayList<SpinnerItem> genders = new ArrayList<SpinnerItem>();
        genders.add(new SpinnerItem("1", "Male"));
        genders.add(new SpinnerItem("2", "Female"));

        return genders;
    }

    public static String getDefaultCityWithCountryData() {
        String cityWithCountryData = "[{id='1', name='USA', city=[{id='5', name='Alabama'}, {id='6', name='Alaska'}, {id='7', name='Arizona'}, {id='8', name='Arkansas'}, {id='9', name='California'}, {id='10', name='Colorado'}, {id='11', name='Connecticut'}, {id='12', name='Delaware'}, {id='13', name='Florida'}, {id='14', name='Georgia'}, {id='15', name='Hawaii'}, {id='16', name='Idaho'}, {id='17', name='Illinois'}, {id='18', name='Indiana'}, {id='19', name='Iowa'}, {id='20', name='Kansas'}, {id='21', name='Kentucky'}, {id='22', name='Louisiana'}, {id='23', name='Maine'}, {id='24', name='Maryland'}, {id='25', name='Massachusetts'}, {id='26', name='Michigan'}, {id='27', name='Minnesota'}, {id='28', name='Mississippi'}, {id='29', name='Missouri'}, {id='30', name='Montana'}, {id='31', name='Nebraska'}, {id='32', name='Nevada'}, {id='33', name='New Hampshire'}, {id='34', name='New Jersey'}, {id='35', name='New Mexico'}, {id='36', name='New York'}, {id='37', name='North Carolina'}, {id='38', name='North Dakota'}, {id='39', name='Ohio'}, {id='40', name='Oklahoma'}, {id='41', name='Oregon'}, {id='42', name='Pennsylvania'}, {id='43', name='Rhode Island'}, {id='44', name='South Carolina'}, {id='45', name='South Dakota'}, {id='46', name='Tennessee'}, {id='47', name='Texas'}, {id='48', name='Utah'}, {id='49', name='Vermont'}, {id='50', name='Virginia'}, {id='51', name='Washington'}, {id='52', name='West Virginia'}, {id='53', name='Wisconsin'}, {id='54', name='Wyoming'}]}]";
        String modifiedCityWithCountry = "{" + "data=" + cityWithCountryData + "}";
        return modifiedCityWithCountry;
    }
}