package com.reversecoder.ci.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.ci.R;
import com.reversecoder.ci.fragment.ContactsFragment;
import com.reversecoder.ci.fragment.HowItWorksFragment;
import com.reversecoder.ci.fragment.PrivacyAndPolicyFragment;
import com.reversecoder.ci.fragment.ProfileFragment;
import com.reversecoder.ci.fragment.TermsAndConditionsFragment;
import com.reversecoder.ci.model.UserData;
import com.reversecoder.ci.model.UserIncome;
import com.reversecoder.ci.model.ResponseUserIncome;
import com.reversecoder.ci.util.AllConstants;
import com.reversecoder.ci.util.AllUrls;
import com.reversecoder.ci.util.AppUtils;
import com.reversecoder.ci.util.FragmentUtilsManager;
import com.reversecoder.ci.util.HttpRequestManager;
import com.reversecoder.residemenu.ResideMenu;
import com.reversecoder.residemenu.ResideMenuItem;

import static com.reversecoder.ci.util.AllConstants.SESSION_IS_USER_LOGGED_IN;
import static com.reversecoder.ci.util.AllConstants.SESSION_USER_DATA;
import static com.reversecoder.ci.util.AllConstants.SESSION_USER_INCOME;
import static com.reversecoder.ci.util.AllConstants.TITLE_FRAGMENT_CONTACTS;
import static com.reversecoder.ci.util.AllConstants.TITLE_FRAGMENT_HOW_IT_WORKS;
import static com.reversecoder.ci.util.AllConstants.TITLE_FRAGMENT_PRIVACY_AND_POLICY;
import static com.reversecoder.ci.util.AllConstants.TITLE_FRAGMENT_PROFILE;
import static com.reversecoder.ci.util.AllConstants.TITLE_FRAGMENT_TERMS_AND_CONDITIONS;
import static com.reversecoder.ci.util.AllConstants.TITLE_MENU_LOGOUT;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ResideMenu resideMenu;
    private ResideMenuItem itemContacts;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemLogout;
    private ResideMenuItem itemHowItWorks;
    private ResideMenuItem itemTermsAndConditions;
    private ResideMenuItem itemPrivacyAndPolicy;
    String TAG = AppUtils.getTagName(HomeActivity.class);

    TextView tvTitle;
    ImageView ivResideMenuLeft, ivResideMenuRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        iniUi(savedInstanceState);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ivResideMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

        ivResideMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    private void iniUi(Bundle savedInstanceState) {
        tvTitle = (TextView) findViewById(R.id.text_title);
        ivResideMenuLeft = (ImageView) findViewById(R.id.reside_menu_left);
        ivResideMenuRight = (ImageView) findViewById(R.id.reside_menu_right);

        setupToolBar();
        setUpMenu();

        if (savedInstanceState == null) {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new ContactsFragment(), AllConstants.TAG_FRAGMENT_CONTACT);
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
        resideMenu.setScaleValue(0.7f);

        // create menu items;
        itemContacts = new ResideMenuItem(this, R.drawable.icon_home, TITLE_FRAGMENT_CONTACTS);
        itemProfile = new ResideMenuItem(this, R.drawable.icon_profile, TITLE_FRAGMENT_PROFILE);
        itemLogout = new ResideMenuItem(this, R.drawable.icon_logout, TITLE_MENU_LOGOUT);
        itemHowItWorks = new ResideMenuItem(this, R.drawable.icon_calendar, TITLE_FRAGMENT_HOW_IT_WORKS);
        itemTermsAndConditions = new ResideMenuItem(this, R.drawable.icon_settings, TITLE_FRAGMENT_TERMS_AND_CONDITIONS);
        itemPrivacyAndPolicy = new ResideMenuItem(this, R.drawable.icon_privacy_and_policy, TITLE_FRAGMENT_PRIVACY_AND_POLICY);

        itemContacts.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemLogout.setOnClickListener(this);
        itemHowItWorks.setOnClickListener(this);
        itemTermsAndConditions.setOnClickListener(this);
        itemPrivacyAndPolicy.setOnClickListener(this);

        resideMenu.addMenuItem(itemContacts, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHowItWorks, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemTermsAndConditions, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemPrivacyAndPolicy, ResideMenu.DIRECTION_RIGHT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemContacts) {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new ContactsFragment(), AllConstants.TAG_FRAGMENT_CONTACT);
        } else if (view == itemProfile) {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new ProfileFragment(), AllConstants.TAG_FRAGMENT_PROFILE);
        } else if (view == itemLogout) {
            SessionManager.setBooleanSetting(HomeActivity.this, SESSION_IS_USER_LOGGED_IN, false);
            Intent logoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (view == itemHowItWorks) {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new HowItWorksFragment(), AllConstants.TAG_FRAGMENT_HOW_IT_WORKS);
        } else if (view == itemTermsAndConditions) {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new TermsAndConditionsFragment(), AllConstants.TAG_FRAGMENT_TERMS_AND_CONDITIONS);
        } else if (view == itemPrivacyAndPolicy) {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new PrivacyAndPolicyFragment(), AllConstants.TAG_FRAGMENT_PRIVACY_AND_POLICY);
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
            if (NetworkManager.isConnected(HomeActivity.this)) {
                new getHeaderInfo(HomeActivity.this).execute();
            } else {
                if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_INCOME))) {
                    Log.d(TAG, "UserIncomeFromSession: " + SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_INCOME));
                    UserIncome userIncome = UserIncome.getResponseObject(SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_INCOME), UserIncome.class);
                    Log.d(TAG, "UserIncomeFromSessionUserIncomeObject: " + userIncome.toString());
                    resideMenu.setHeaderTitle(userIncome.getFirst_name() + " " + userIncome.getLast_name());
                    resideMenu.setHeaderSubtitle(HomeActivity.this.getResources().getString(R.string.txt_your_earning) + userIncome.getTotal_income());
                } else {
                    UserData user = UserData.getResponseObject(SessionManager.getStringSetting(HomeActivity.this, SESSION_USER_DATA), UserData.class);
                    resideMenu.setHeaderTitle(user.getFirst_name() + " " + user.getLast_name());
                    resideMenu.setHeaderSubtitle(HomeActivity.this.getResources().getString(R.string.txt_your_earning) + "___");
                }
            }
        }

        @Override
        public void closeMenu() {
//            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public class getHeaderInfo extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public getHeaderInfo(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            UserData userData = UserData.getResponseObject(SessionManager.getStringSetting(mContext, AllConstants.SESSION_USER_DATA), UserData.class);
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getUserIncomeUrl(userData.getId()));
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response: " + result.getResult().toString());
                ResponseUserIncome responseData = ResponseUserIncome.getResponseObject(result.getResult().toString(), ResponseUserIncome.class);

                if ((responseData.getStatus().equalsIgnoreCase("1")) && (responseData.getData().size() == 1)) {
                    Log.d(TAG, "success wrapper: " + responseData.getData().get(0).toString());
                    SessionManager.setStringSetting(mContext, SESSION_USER_INCOME, responseData.getData().get(0).toString());
                    Log.d(TAG, "session Async: " + SessionManager.getStringSetting(mContext, SESSION_USER_INCOME));

                    UserIncome userIncome = UserIncome.getResponseObject(SessionManager.getStringSetting(mContext, SESSION_USER_INCOME), UserIncome.class);

                    //set header info
                    resideMenu.setHeaderTitle(userIncome.getFirst_name() + " " + userIncome.getLast_name());
                    resideMenu.setHeaderSubtitle(mContext.getResources().getString(R.string.txt_your_earning) + userIncome.getTotal_income());
                }
            }
        }
    }
}
