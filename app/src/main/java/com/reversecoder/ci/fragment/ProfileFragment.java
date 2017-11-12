package com.reversecoder.ci.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reversecoder.ci.util.AllConstants;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.permission.util.SessionManager;
import com.reversecoder.ci.R;
import com.reversecoder.ci.activity.HomeActivity;
import com.reversecoder.ci.adapter.UserSpinnerAdapter;
import com.reversecoder.ci.model.CityWithCountry;
import com.reversecoder.ci.model.CityWithCountryData;
import com.reversecoder.ci.model.ResponseCityWithCountry;
import com.reversecoder.ci.model.ResponseUserData;
import com.reversecoder.ci.model.SpinnerItem;
import com.reversecoder.ci.model.UserData;
import com.reversecoder.ci.util.AllUrls;
import com.reversecoder.ci.util.AppUtils;
import com.reversecoder.ci.util.HttpRequestManager;
import com.reversecoder.residemenu.ResideMenu;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import static com.reversecoder.ci.util.AllConstants.SESSION_CITY_WITH_COUNTRY;
import static com.reversecoder.ci.util.AllConstants.SESSION_USER_DATA;
import static com.reversecoder.ci.util.AllConstants.TAG_FRAGMENT_DATE_PICKER;
import static com.reversecoder.ci.util.AllConstants.getGenderData;
import static com.wdullaer.materialdatetimepicker.Utils.getMonthName;
import static com.wdullaer.materialdatetimepicker.Utils.getMonthNumber;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ProfileFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private Button btnUpdateProfile;
    private EditText edtFirstName, edtLastName, edtEmail, edtPassword;
    Spinner spinnerGender, spinnerCity, spinnerCountry;
    private TextView tvDateOfBirth;
    ProgressDialog loadingDialog;
    DoUpdateProfile doUpdateUser;
    UserData user;
    String TAG = AppUtils.getTagName(ProfileFragment.class);
    UserSpinnerAdapter spinnerGenderAdapter, spinnerCityAdapter, spinnerCountryAdapter;
    DatePickerDialog datePickerDialog;
    int lastSelectedYear = -1, lastSelectedDay = -1, lastSelectedMonth = -1;
    CityWithCountryData wrapperCityWithCountryData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpViews();
        setUpActions();
        return parentView;
    }

    private void setUpViews() {
        HomeActivity parentActivity = (HomeActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        ((HomeActivity) getActivity()).setTitle(getString(R.string.title_fragment_profile));

        btnUpdateProfile = (Button) parentView.findViewById(R.id.btn_update_profile);
        edtFirstName = (EditText) parentView.findViewById(R.id.edt_first_name);
        edtLastName = (EditText) parentView.findViewById(R.id.edt_last_name);
        spinnerGender = (Spinner) parentView.findViewById(R.id.spinner_gender);
        spinnerCity = (Spinner) parentView.findViewById(R.id.spinner_city);
        spinnerCountry = (Spinner) parentView.findViewById(R.id.spinner_country);
        tvDateOfBirth = (TextView) parentView.findViewById(R.id.tv_date_of_birth);
        edtEmail = (EditText) parentView.findViewById(R.id.edt_email);
        edtPassword = (EditText) parentView.findViewById(R.id.edt_password);

        // set gender spinnner
        spinnerGenderAdapter = new UserSpinnerAdapter(getActivity(), UserSpinnerAdapter.ADAPTER_TYPE.GENDER);
        spinnerGender.setAdapter(spinnerGenderAdapter);

        //set city spinner
        spinnerCityAdapter = new UserSpinnerAdapter(getActivity(), UserSpinnerAdapter.ADAPTER_TYPE.CITY);
        spinnerCity.setAdapter(spinnerCityAdapter);

        //set country spinner
        spinnerCountryAdapter = new UserSpinnerAdapter(getActivity(), UserSpinnerAdapter.ADAPTER_TYPE.COUNTRY);
        spinnerCountry.setAdapter(spinnerCountryAdapter);

        if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_USER_DATA))) {
            user = UserData.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_USER_DATA), UserData.class);

            if (NetworkManager.isConnected(getActivity())) {
                new GetCityWithCountry(getActivity()).execute();
            } else {
                if (AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_CITY_WITH_COUNTRY))) {
                    SessionManager.setStringSetting(getActivity(), SESSION_CITY_WITH_COUNTRY, AllConstants.getDefaultCityWithCountryData());
                }

                wrapperCityWithCountryData = CityWithCountryData.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_CITY_WITH_COUNTRY), CityWithCountryData.class);
                Log.d(TAG, "success response from session Object: " + wrapperCityWithCountryData.getData().size());

                setUserData();
            }
        }
    }

    private void setUpActions() {

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem item = (SpinnerItem) parent.getItemAtPosition(position);
//                Toast.makeText(SignUpActivity.this, item.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem item = (SpinnerItem) parent.getItemAtPosition(position);
//                Toast.makeText(SignUpActivity.this, item.getName(), Toast.LENGTH_LONG).show();

                spinnerCityAdapter.setData(wrapperCityWithCountryData.getCity(item.getName()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem item = (SpinnerItem) parent.getItemAtPosition(position);
//                Toast.makeText(SignUpActivity.this, item.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tvDateOfBirth.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Calendar now = Calendar.getInstance();
                datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String selectedDate = dayOfMonth + " " + getMonthName(monthOfYear, true) + ", " + year;
                                tvDateOfBirth.setText(selectedDate);
                                lastSelectedDay = dayOfMonth;
                                lastSelectedMonth = monthOfYear;
                                lastSelectedYear = year;
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.vibrate(true);
                datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.colorPrimary));
                datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
                if (lastSelectedYear != -1 && lastSelectedDay != -1 && lastSelectedMonth != -1) {
                    datePickerDialog.setDate(lastSelectedYear, lastSelectedMonth, lastSelectedDay);
                } else {

                    if (!AppUtils.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), SESSION_USER_DATA))) {
                        user = UserData.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_USER_DATA), UserData.class);
                        if (!AppUtils.isNullOrEmpty(user.getDate_of_birth())) {
                            String mDate = user.getDate_of_birth();
                            if (mDate.split(",").length > 0) {
                                int year = Integer.parseInt(mDate.split(",")[1].trim());
                                int month = getMonthNumber(mDate.split(",")[0].split(" ")[1].trim());
                                int day = Integer.parseInt(mDate.split(",")[0].split(" ")[0].trim());
                                datePickerDialog.setDate(year, month, day);
                            }
                        }
                    }
                }
                datePickerDialog.show(getFragmentManager(), TAG_FRAGMENT_DATE_PICKER);
            }
        });

        btnUpdateProfile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                String mId = "", mEmail = edtEmail.getText().toString(), mPassword = edtPassword.getText().toString(),
                        mFirstName = edtFirstName.getText().toString(), mLastName = edtLastName.getText().toString(),
                        mGender = ((SpinnerItem) spinnerGender.getSelectedItem()).getName(),
                        mCity = (((SpinnerItem) spinnerCity.getSelectedItem()).getName().equalsIgnoreCase("Choose Your Location") ? "" : ((SpinnerItem) spinnerCity.getSelectedItem()).getName()),
                        mCountry = ((SpinnerItem) spinnerCountry.getSelectedItem()).getName(),
                        mDateOfBirth = (tvDateOfBirth.getText().toString().equalsIgnoreCase(getString(R.string.txt_date_of_birth)) ? "" : tvDateOfBirth.getText().toString());
                if (user != null) {
                    mId = user.getId();
                }

                if (mFirstName.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_first_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mLastName.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_last_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mGender.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_gender_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mDateOfBirth.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_date_of_birth_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mCountry.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_country_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mCity.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_city_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEmail.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_empty_password_field), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                doUpdateUser = new DoUpdateProfile(getActivity(), mId, mEmail, mPassword, mFirstName, mLastName, mGender, mCity, mCountry, mDateOfBirth);
                doUpdateUser.execute();
            }
        });
    }

    public class DoUpdateProfile extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mId = "", mEmail = "", mPassword = "", mFirstName = "", mLastName = "", mGender = "", mCity = "", mCountry = "", mDateOfBirth = "";

        public DoUpdateProfile(Context context, String id, String email, String password, String firstName, String lastName, String gender, String city, String country, String dateOfBirth) {
            this.mContext = context;
            this.mId = id;
            this.mEmail = email;
            this.mPassword = password;
            this.mFirstName = firstName;
            this.mLastName = lastName;
            this.mGender = gender;
            this.mCity = city;
            this.mCountry = country;
            this.mDateOfBirth = dateOfBirth;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setMessage(mContext.getResources().getString(
                    R.string.dialog_loading));
            loadingDialog.setIndeterminate(false);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    if (loadingDialog != null
                            && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getUpdateUserUrl(), AllUrls.getUpdateUserParameters(mId, mEmail, mPassword, mFirstName, mLastName, mGender, mCity, mCountry, mDateOfBirth), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {

            if (loadingDialog != null
                    && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response: " + result.getResult().toString());
                ResponseUserData responseData = ResponseUserData.getResponseObject(result.getResult().toString(), ResponseUserData.class);

                if ((responseData.getStatus().equalsIgnoreCase("1")) && (responseData.getUser_data().size() == 1)) {
                    Log.d(TAG, "success wrapper: " + responseData.getUser_data().get(0).toString());
                    SessionManager.setStringSetting(getActivity(), SESSION_USER_DATA, responseData.getUser_data().get(0).toString());

                    Toast.makeText(getActivity(), responseData.getMsg(), Toast.LENGTH_SHORT).show();
                    lastSelectedDay = -1;
                    lastSelectedMonth = -1;
                    lastSelectedYear = -1;
                } else {
                    Toast.makeText(getActivity(), responseData.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetCityWithCountry extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public GetCityWithCountry(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            HttpRequestManager.HttpResponse response = HttpRequestManager.doGetRequest(AllUrls.getAllCityWithCountryUrl());
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response: " + result.getResult().toString());
                ResponseCityWithCountry responseData = ResponseCityWithCountry.getResponseObject(result.getResult().toString(), ResponseCityWithCountry.class);
                Log.d(TAG, "success response from object: " + responseData.toString());

                if ((responseData.getStatus().equalsIgnoreCase("1")) && (responseData.getData().size() > 0)) {
                    Log.d(TAG, "success response from cityWithCountry: " + responseData.getData().toString());
                    String modifiedCityWithCountry = "{" + "data=" + responseData.getData().toString() + "}";
                    SessionManager.setStringSetting(mContext, SESSION_CITY_WITH_COUNTRY, modifiedCityWithCountry);
                    Log.d(TAG, "success response from session: " + SessionManager.getStringSetting(mContext, SESSION_CITY_WITH_COUNTRY));
                    wrapperCityWithCountryData = CityWithCountryData.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_CITY_WITH_COUNTRY), CityWithCountryData.class);

                    setUserData();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setUserData() {

        if (user != null) {
            edtFirstName.setText(user.getFirst_name());
            edtLastName.setText(user.getLast_name());
            if (!AppUtils.isNullOrEmpty(user.getDate_of_birth())) {
                tvDateOfBirth.setText(user.getDate_of_birth());
            }
            edtEmail.setText(user.getEmail());
            edtPassword.setText(user.getPassword());

            spinnerGenderAdapter.setData(getGenderData());
            spinnerGender.setSelection(spinnerGenderAdapter.getItemPosition(user.getGender()));

            wrapperCityWithCountryData = CityWithCountryData.getResponseObject(SessionManager.getStringSetting(getActivity(), SESSION_CITY_WITH_COUNTRY), CityWithCountryData.class);
            spinnerCountryAdapter.setData(wrapperCityWithCountryData.getCountry());
            spinnerCountry.setSelection(spinnerCountryAdapter.getItemPosition(user.getCountry()));

            spinnerCityAdapter.setData(wrapperCityWithCountryData.getCity(((SpinnerItem) spinnerCountry.getSelectedItem()).getName()));
            if (!AppUtils.isNullOrEmpty(user.getCity())) {
                spinnerCity.setSelection(spinnerCityAdapter.getItemPosition(user.getCity()));
            }
        }
    }
}
