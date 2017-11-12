package com.reversecoder.ci.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.ci.R;
import com.reversecoder.ci.adapter.UserSpinnerAdapter;
import com.reversecoder.ci.model.SpinnerItem;
import com.reversecoder.ci.model.ResponseUserData;
import com.reversecoder.ci.util.AllUrls;
import com.reversecoder.ci.util.AppUtils;
import com.reversecoder.ci.util.HttpRequestManager;

import static com.reversecoder.ci.util.AllConstants.SESSION_USER_DATA;
import static com.reversecoder.ci.util.AllConstants.getGenderData;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class SignUpActivity extends AppCompatActivity {

    Button btnLogin, btnSignUp;
    EditText edtFirstName, edtLastName, edtEmail, edtPassword;
    Spinner spinnerGender;
    TextView tvTitle;
    ProgressDialog loadingDialog;
    DoSignUp doSignUpUser;
    String TAG = AppUtils.getTagName(SignUpActivity.class);
    UserSpinnerAdapter spinnerGenderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initRegistrationUI();
        initRegistrationAction();
    }

    private void initRegistrationUI() {
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnLogin = (Button) findViewById(R.id.btn_login);
        edtFirstName = (EditText) findViewById(R.id.edt_first_name);
        edtLastName = (EditText) findViewById(R.id.edt_last_name);
        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvTitle = (TextView) findViewById(R.id.text_title);

        spinnerGenderAdapter = new UserSpinnerAdapter(SignUpActivity.this, UserSpinnerAdapter.ADAPTER_TYPE.GENDER);
        spinnerGender.setAdapter(spinnerGenderAdapter);
        spinnerGenderAdapter.setData(getGenderData());
        spinnerGender.setSelection(0);

//        tvTitle.setText("Sign Up");
//        edtFirstName.setText("Md. Rashadul");
//        edtLastName.setText("Alam");
//        edtEmail.setText("rashed.droid@gmail.com");
//        edtPassword.setText("123456");

        setupToolBar();
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void initRegistrationAction() {

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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail = edtEmail.getText().toString(), mPassword = edtPassword.getText().toString(),
                        mFirstName = edtFirstName.getText().toString(), mLastName = edtLastName.getText().toString(),
                        mGender = (((SpinnerItem) spinnerGender.getSelectedItem()).getName().equalsIgnoreCase(getString(R.string.txt_none)) ? "" : ((SpinnerItem) spinnerGender.getSelectedItem()).getName());

                if (mFirstName.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_empty_first_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mLastName.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_empty_last_name_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mGender.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_empty_gender_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEmail.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_empty_email_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_empty_password_field), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkManager.isConnected(SignUpActivity.this)) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                doSignUpUser = new DoSignUp(SignUpActivity.this, mEmail, mPassword, mFirstName, mLastName, mGender);
                doSignUpUser.execute();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class DoSignUp extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;
        private String mEmail = "", mPassword = "", mFirstName = "", mLastName = "", mGender = "";

        public DoSignUp(Context context, String email, String password, String firstName, String lastName, String gender) {
            mContext = context;
            mEmail = email;
            mPassword = password;
            mFirstName = firstName;
            mLastName = lastName;
            mGender = gender;
        }

        @Override
        protected void onPreExecute() {
            loadingDialog = new ProgressDialog(mContext);
            loadingDialog.setMessage(getResources().getString(
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
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getSignUpUrl(), AllUrls.getSignUpParameters(mEmail, mPassword, mFirstName, mLastName, mGender), null);
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
                    SessionManager.setStringSetting(SignUpActivity.this, SESSION_USER_DATA, responseData.getUser_data().get(0).toString());

                    Toast.makeText(SignUpActivity.this, responseData.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, responseData.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
