package com.reversecoder.ci.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;

import com.diroag.floatingwindows.service.FloatingWindowController;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.ci.floatingwindow.AdvertiseView;

import java.io.IOException;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.INCOMING_CALL_RECEIVED;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.INCOMING_CALL_REJECTED_or_INCOMING_MISSED_CALL;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.INCOMING_CALL_RINGING;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.NONE;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.OUTGOING_CALL_REJECTED_or_OUTGOING_MISSED_CALL_or_ANSWERED_CALL_ENDED;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.OUTGOING_CALL_RINGING_or_BOTH_ARE_TALKING_or_CALL_HELD_or_CALL_WAITING;
import static com.reversecoder.ci.broadcast.PhoneCallReceiver.CALL_STATE.OUTGOING_CALL_STARTED;
import static com.reversecoder.ci.util.AllConstants.SESSION_IS_USER_LOGGED_IN;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class PhoneCallReceiver extends BroadcastReceiver {

    String incomingNumber;
    private static String outgoingNumber;
    private static int previousState;
    private String TAG = PhoneCallReceiver.class.getSimpleName();
    public static final int CALL_STATE_OUTGOING = 3;
    public static CALL_STATE callState = NONE;
    Intent intentCallState;
    String intentStatus = "", intentMessage = "";
    AdvertiseView mAdvertiseView;
    FloatingWindowController floatingWindowController;

    public enum CALL_STATE {
        OUTGOING_CALL_STARTED, OUTGOING_CALL_RINGING_or_BOTH_ARE_TALKING_or_CALL_HELD_or_CALL_WAITING,
        INCOMING_CALL_RINGING, INCOMING_CALL_RECEIVED, INCOMING_CALL_REJECTED_or_INCOMING_MISSED_CALL,
        OUTGOING_CALL_REJECTED_or_OUTGOING_MISSED_CALL_or_ANSWERED_CALL_ENDED, NONE
    }

    public int outgoingCallStarted = 1, outgoingCallRingingOrBothAreTalkingOrCallHeldOrCallWaiting = 1,
            incomingCallRinging = 1, incomingCallReceived = 1, incomingCallRejectedOrIncomingMissedCall = 1,
            outgoingCallRejectedOrOutgoingMissedCallOrAnsweredCallEnded = 1, none = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        onCallStateChanged(context, intent);
    }

    private void onCallStateChanged(final Context context, Intent intent) {
        if (null == intent) {
            Log.d(TAG, "got null intent");
            return;
        }

        try {
            Thread.sleep(500);
            Log.d(TAG, "talking sleep 500 ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String action = intent.getAction();
        Log.d(TAG, "current action: " + action);

        if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            outgoingNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            previousState = CALL_STATE_OUTGOING;
            callState = OUTGOING_CALL_STARTED;
//            Log.d(TAG, "Outgoing call started");

            //Save current status for sending to the PhoneCallAdvertiseActivity
            intentStatus = callState.name();
            intentMessage = "Outgoing call started to: "+outgoingNumber;

            Log.d(TAG, "current status(1):" + intentStatus);
            Log.d(TAG, "current message:(1)" + intentMessage);
//
//            try {
//                Thread.sleep(2000);
//                Log.d(TAG, "talking sleep 500 ms");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//            showAdvertiseView(context, outgoingNumber);

        } else {
            final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incoming_Number) {
                    super.onCallStateChanged(state, incoming_Number);

                    if (state == previousState) {
                        return;
                    }

                    if (incoming_Number != null && incoming_Number.length() > 0) {
                        incomingNumber = incoming_Number;
                    }

                    switch (state) {
                        case TelephonyManager.CALL_STATE_RINGING:
//                            Log.d(TAG, "CALL_STATE_RINGING");
                            //Incoming call ringing
//                            Log.d(TAG, "Incoming call ringing");
                            callState = INCOMING_CALL_RINGING;
                            previousState = state;

                            //Save current status for sending to the PhoneCallAdvertiseActivity
                            intentStatus = callState.name();
                            intentMessage = "Incoming call ringing";

                            Log.d(TAG, "current status(2):" + intentStatus);
                            Log.d(TAG, "current message:(2)" + intentMessage);
                            Log.d(TAG, "current incoming no:(4)" + incomingNumber);

                            showAdvertiseView(context, incomingNumber);

                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
//                            Log.d(TAG, "CALL_STATE_OFFHOOK");
                            if (previousState == TelephonyManager.CALL_STATE_RINGING) {
                                // Incoming call received
//                                Log.d(TAG, "Incoming call received");
                                callState = INCOMING_CALL_RECEIVED;

                                //Save current status for sending to the PhoneCallAdvertiseActivity
                                intentStatus = callState.name();
                                intentMessage = "Incoming call received";

                                Log.d(TAG, "current status(3):" + intentStatus);
                                Log.d(TAG, "current message:(3)" + intentMessage);
                            } else {
                                // Outgoing call ringing, or both are talking or held or waiting
//                                Log.d(TAG, "Outgoing call ringing, or both are talking or held or waiting");
                                callState = OUTGOING_CALL_RINGING_or_BOTH_ARE_TALKING_or_CALL_HELD_or_CALL_WAITING;

                                //Save current status for sending to the PhoneCallAdvertiseActivity
                                intentStatus = callState.name();
                                intentMessage = "Outgoing call ringing, or both are talking or held or waiting";

                                Log.d(TAG, "current status(4):" + intentStatus);
                                Log.d(TAG, "current message:(4)" + intentMessage);
                                Log.d(TAG, "current outgoing no:(4)" + outgoingNumber);

                                showAdvertiseView(context, outgoingNumber);
                            }

                            previousState = state;

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
//                            Log.d(TAG, "CALL_STATE_IDLE==>" + incomingNumber);
                            if ((previousState == TelephonyManager.CALL_STATE_OFFHOOK)) {
                                //Outgoing call rejected or outgoing missed call or answered call ended
//                                Log.d(TAG, "Outgoing call rejected or outgoing missed call or answered call ended");
                                callState = OUTGOING_CALL_REJECTED_or_OUTGOING_MISSED_CALL_or_ANSWERED_CALL_ENDED;

                                //Save current status for sending to the PhoneCallAdvertiseActivity
                                intentStatus = callState.name();
                                intentMessage = "Outgoing call rejected or outgoing missed call or answered call ended";

                                Log.d(TAG, "current status(5):" + intentStatus);
                                Log.d(TAG, "current message:(5)" + intentMessage);

                            } else if ((previousState == TelephonyManager.CALL_STATE_RINGING)) {
                                //Incoming call rejected or incoming Missed call
//                                Log.d(TAG, "Incoming call rejected or incoming Missed call");
                                callState = INCOMING_CALL_REJECTED_or_INCOMING_MISSED_CALL;

                                //Save current status for sending to the PhoneCallAdvertiseActivity
                                intentStatus = callState.name();
                                intentMessage = "Incoming call rejected or incoming Missed call";

                                Log.d(TAG, "current status(6):" + intentStatus);
                                Log.d(TAG, "current message:(6)" + intentMessage);

                            }
                            previousState = state;

                            closeAdvertiseView(context);

                            break;
                    }

                    Log.d(TAG, "current status(final):" + intentStatus);
                    Log.d(TAG, "current message:(final)" + intentMessage);

//                    //Send update to the PhoneCallAdvertiseActivity
//                    if (!intentStatus.equalsIgnoreCase("") && !intentMessage.equalsIgnoreCase("")) {
//                        Log.d(TAG, "Sending update to PhoneCallAdvertiseActivity");
//                        intentCallState = new Intent(context, PhoneCallAdvertiseActivity.class);
//                        intentCallState.putExtra(KEY_INTENT_STATUS, intentStatus);
//                        intentCallState.putExtra(KEY_INTENT_MESSAGE, intentMessage);
//                        intentCallState.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intentCallState);
//                    }

                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }

    }

    public void showAdvertiseView(Context context, String sendSmsTo) {
        if (SessionManager.getBooleanSetting(context, SESSION_IS_USER_LOGGED_IN, false)) {
            if (mAdvertiseView == null) {
                mAdvertiseView = new AdvertiseView(context, "AdvertiseView", sendSmsTo);
            } else {
                if (mAdvertiseView.isWindowShowed()) {
                    return;
                }
            }

            if (floatingWindowController == null) {
                floatingWindowController = FloatingWindowController.create(context.getApplicationContext());
            }

            if (!mAdvertiseView.isWindowShowed()) {
                floatingWindowController.showAtLocation(mAdvertiseView, Gravity.RIGHT, 0, 0);
            }
        }
    }

    public void closeAdvertiseView(Context context) {
        if (mAdvertiseView != null) {
            if(mAdvertiseView.isWindowShowed()){
                mAdvertiseView.dismiss();
            }
        }
    }

    public static void rejectCall(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            com.android.internal.telephony.ITelephony telephonyService = (com.android.internal.telephony.ITelephony) method.invoke(telephonyManager);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void acceptCall(Context context) {
//        try {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            Class clazz = Class.forName(telephonyManager.getClass().getName());
//            Method method = clazz.getDeclaredMethod("getITelephony");
//            method.setAccessible(true);
//            com.android.internal.telephony.ITelephony telephonyService = (com.android.internal.telephony.ITelephony) method.invoke(telephonyManager);
//            telephonyService.answerRingingCall();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    //another solution
//        Intent btnPickCall = new Intent(Intent.ACTION_MEDIA_BUTTON);
//        KeyEvent callPickEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
//        btnPickCall.putExtra(Intent.EXTRA_KEY_EVENT, callPickEvent);
//        context.sendOrderedBroadcast(btnPickCall, "android.permission.CALL_PRIVILEGED");
//        Log.d(PhoneCallReceiver.class.getSimpleName(),"Answer call: "+"Accepted");

//        try {
//            Process proc = Runtime.getRuntime().exec("su");
//            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
//
//            os.writeBytes("service call phone 5\n");
//            os.flush();
//
//            os.writeBytes("exit\n");
//            os.flush();
//
//            if (proc.waitFor() == 255) {
//                // TODO handle being declined root access
//                // 255 is the standard code for being declined root for SU
//            }
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

    private void acceptCall(Context context) {
        try {

            try {
                // logger.debug("execute input keycode headset hook");
                Runtime.getRuntime().exec("input keyevent " +
                        Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

            } catch (IOException e) {
                // Runtime.exec(String) had an I/O problem, try to fall back
                //    logger.debug("send keycode headset hook intents");
                String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_HEADSETHOOK));
                Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                        Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_HEADSETHOOK));

                context.sendOrderedBroadcast(btnDown, enforcedPerm);
                context.sendOrderedBroadcast(btnUp, enforcedPerm);
            }
        } finally {
        }
    }

    public static void muteCall(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            com.android.internal.telephony.ITelephony telephonyService = (com.android.internal.telephony.ITelephony) method.invoke(telephonyManager);
            telephonyService.silenceRinger();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}