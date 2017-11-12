package com.reversecoder.ci.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.ci.R;
import com.reversecoder.ci.broadcast.PhoneCallReceiver;
import com.rodolfonavalon.shaperipplelibrary.ShapeRipple;
import com.rodolfonavalon.shaperipplelibrary.model.Circle;

import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.NONE;
import static com.reversecoder.ci.util.AllConstants.KEY_INTENT_MESSAGE;
import static com.reversecoder.ci.util.AllConstants.KEY_INTENT_STATUS;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class PhoneCallAdvertiseActivity extends AppCompatActivity {

    Button btnAcceptCall, btnMuteCall, btnRejectCall;
    ShapeRipple ripple;
    FinishCallCountDownTimer finishCallCountDownTimer;
    private final long startTime = 3 * 1000;
    private final long interval = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_phone_call_advertise);

        initPhoneView();
        initPhoneViewAtion();
    }

    public void initPhoneView() {
        btnAcceptCall = (Button) findViewById(R.id.btn_accept_call);
        btnMuteCall = (Button) findViewById(R.id.btn_mute_call);
        btnRejectCall = (Button) findViewById(R.id.btn_reject_call);
        ripple = (ShapeRipple) findViewById(R.id.background_ripple);
        ripple.setRippleShape(new Circle());
        ripple.setEnableColorTransition(true);
        ripple.setEnableSingleRipple(false);
        ripple.setEnableRandomPosition(true);
        ripple.setEnableRandomColor(true);
        ripple.setEnableStrokeStyle(false);

        ripple.setRippleDuration(2500);
        ripple.setRippleCount(10);
        ripple.setRippleMaximumRadius(184);

    }

    public void initPhoneViewAtion() {
        btnAcceptCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                Toast.makeText(PhoneCallAdvertiseActivity.this, "Accept button clicked", Toast.LENGTH_SHORT).show();
//                PhoneCallReceiver.acceptCall(PhoneCallAdvertiseActivity.this);
            }
        });

        btnMuteCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                PhoneCallReceiver.muteCall(PhoneCallAdvertiseActivity.this);
            }
        });

        btnRejectCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                PhoneCallReceiver.rejectCall(PhoneCallAdvertiseActivity.this);
            }
        });
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//
//        initPhoneView();
//        initPhoneViewAtion();
//    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String status = "";
        String message = "";
        PhoneCallReceiver.CALL_STATE callState = PhoneCallReceiver.CALL_STATE.NONE;
        try {
            status = intent.getStringExtra(KEY_INTENT_STATUS);
            message = intent.getStringExtra(KEY_INTENT_MESSAGE);
            callState = PhoneCallReceiver.CALL_STATE.valueOf(status);
        } catch (Exception ex) {
            ex.printStackTrace();
            callState = PhoneCallReceiver.CALL_STATE.NONE;
        }
        Toast.makeText(PhoneCallAdvertiseActivity.this, message, Toast.LENGTH_SHORT).show();

        switch (callState) {
            case OUTGOING_CALL_STARTED:
                break;
            case OUTGOING_CALL_RINGING_or_BOTH_ARE_TALKING_or_CALL_HELD_or_CALL_WAITING:
                break;
            case INCOMING_CALL_RINGING:
                break;
            case INCOMING_CALL_RECEIVED:
                break;
            case INCOMING_CALL_REJECTED_or_INCOMING_MISSED_CALL:
                finishCallCountDownTimer = new FinishCallCountDownTimer(startTime, interval);
                finishCallCountDownTimer.start();
                break;
            case OUTGOING_CALL_REJECTED_or_OUTGOING_MISSED_CALL_or_ANSWERED_CALL_ENDED:
                finishCallCountDownTimer = new FinishCallCountDownTimer(startTime, interval);
                finishCallCountDownTimer.start();
                break;
        }
    }

    public class FinishCallCountDownTimer extends CountDownTimer {
        public FinishCallCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            PhoneCallReceiver.callState = NONE;
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }
}
