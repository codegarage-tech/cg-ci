package com.reversecoder.ci.floatingwindow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.diroag.floatingwindows.service.FloatingWindowView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.ci.R;
import com.reversecoder.ci.model.Advertisement;
import com.reversecoder.ci.model.AdvertisementCount;
import com.reversecoder.ci.model.UserData;
import com.reversecoder.ci.util.AllConstants;
import com.reversecoder.ci.util.AllUrls;
import com.reversecoder.ci.util.AppUtils;
import com.reversecoder.ci.util.HttpRequestManager;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AdvertiseView extends FloatingWindowView {

    private String mRequirementId;
    Button btnLock, btnClose;
    ImageView ivAdd;
    View floatingWindow;
    String TAG = AppUtils.getTagName(AdvertiseView.class);
    Advertisement advertisementData;
    AdvertisementCount advertisementCount;
    UserAdvertiseCount userAdvertiseCount;
    String smsNumber="";

    public AdvertiseView(Context context, String requirementId, String sendSMSTo) {
        super(context);
        if (requirementId == null)
            throw new NullPointerException("requirementId can't be null");
        mRequirementId = requirementId;
        smsNumber = sendSMSTo;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater) {
        floatingWindow = layoutInflater.inflate(R.layout.floating_window_advertise, null, false);
        initUI();
        initAction();
        return floatingWindow;
    }


    private void initUI() {
        btnLock = (Button) floatingWindow.findViewById(R.id.btn_lock);
        btnClose = (Button) floatingWindow.findViewById(R.id.btn_close);
        ivAdd = (ImageView) floatingWindow.findViewById(R.id.iv_add);

        if (NetworkManager.isConnected(getContext())) {
            Glide
                    .with(getContext())
                    .load(R.drawable.gif_loading)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(ivAdd);
            new showAdvertisement(getContext()).execute();
        } else {
            Glide
                    .with(getContext())
                    .load(R.drawable.gif_no_internet)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(ivAdd);
        }
    }

    private void initAction() {

        ivAdd.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                try {

                    if ((advertisementData != null) && (advertisementData.getStatus().equalsIgnoreCase("1"))) {
                        if (NetworkManager.isConnected(getContext())) {
                            userAdvertiseCount = new UserAdvertiseCount(getContext());
                            userAdvertiseCount.execute();
                        }

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertisementData.getLink()));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(browserIntent);

                        dismiss();
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AllUrls.APP_WEB_URL));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(browserIntent);

                        dismiss();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    dismiss();
                    if(userAdvertiseCount !=null){
                        userAdvertiseCount.cancel(true);
                    }
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocked()) {
                    unlockPosition();
                    btnLock.setBackgroundResource(R.drawable.ic_unlock);
                    Toast.makeText(getContext(), "Unlocked!", Toast.LENGTH_SHORT).show();
                    return;
                }
                lockPosition();
                btnLock.setBackgroundResource(R.drawable.ic_lock);
                Toast.makeText(getContext(), "Locked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class showAdvertisement extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public showAdvertisement(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            UserData userData = UserData.getResponseObject(SessionManager.getStringSetting(mContext, AllConstants.SESSION_USER_DATA), UserData.class);
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getShowAdvertisementUrl(), AllUrls.getShowAdvertisementParameters(userData.getId(), smsNumber), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response: " + result.getResult().toString());
                advertisementData = Advertisement.getResponseObject(result.getResult().toString(), Advertisement.class);
                Log.d(TAG, "Advertisement: " + advertisementData.toString());

                if (advertisementData != null && advertisementData.getStatus().equalsIgnoreCase("1")) {
                    Glide
                            .with(getContext())
                            .load(advertisementData.getImage())
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                            .into(ivAdd);
                } else {
                    Glide
                            .with(getContext())
                            .load(R.drawable.gif_no_result)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                            .into(ivAdd);
                }
            }
        }
    }

    public class UserAdvertiseCount extends AsyncTask<String, String, HttpRequestManager.HttpResponse> {

        private Context mContext;

        public UserAdvertiseCount(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpRequestManager.HttpResponse doInBackground(String... params) {
            UserData userData = UserData.getResponseObject(SessionManager.getStringSetting(mContext, AllConstants.SESSION_USER_DATA), UserData.class);
            HttpRequestManager.HttpResponse response = HttpRequestManager.doRestPostRequest(AllUrls.getUserAdvertiseCountUrl(), AllUrls.getUserAdvertiseCountParameters(userData.getId(), advertisementData.getId()), null);
            return response;
        }

        @Override
        protected void onPostExecute(HttpRequestManager.HttpResponse result) {
            if (result.isSuccess() && !AppUtils.isNullOrEmpty(result.getResult().toString())) {
                Log.d(TAG, "success response: " + result.getResult().toString());

                advertisementCount = AdvertisementCount.getResponseObject(result.getResult().toString(), AdvertisementCount.class);
                Log.d(TAG, "AdvertisementCount: " + advertisementCount.toString());

                if (advertisementCount != null && advertisementCount.getStatus().equalsIgnoreCase("1")) {
                    Log.d(TAG, "AdvertisementCount: Advertise successfully counted");
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.txt_add_is_counted), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "AdvertisementCount: Advertise is not counted");
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.txt_error_while_counting_add), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}