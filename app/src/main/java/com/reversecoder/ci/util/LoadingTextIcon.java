package com.reversecoder.ci.util;

import android.os.AsyncTask;
import android.widget.TextView;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class LoadingTextIcon extends AsyncTask<String, String, String> {

    private TextView statusText;
    private static boolean isRequestedForExist = false;
    private String appendedText = "";

    public LoadingTextIcon(TextView textView, String textNeedToAppendBeforeLoadingIcon) {
        statusText = textView;
        appendedText = textNeedToAppendBeforeLoadingIcon;
    }

    @Override
    public String doInBackground(String... params) {
        String shape = "/";
        int iCounter = 0; //1 unit = 250ms
        while (true) {
            iCounter++;
            switch (shape) {
                case "/":
                    shape = "\\";
                    break;
                case "\\":
                    shape = "--";
                    break;
                case "--":
                    shape = "/";
                    break;
            }
            //Update Animation
            publishProgress(appendedText + shape);
            //Sleep for 250ms;
            try {
                Thread.sleep(250);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (iCounter == 50) {
                //let's do some checking
                if (isRequestedForExist) {
                    break;
                } else {
                    //reset
                    iCounter = 0;
                }

            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        isRequestedForExist = false;
    }

    @Override
    protected void onProgressUpdate(String... text) {
        statusText.setText(text[0]);
    }

    @Override
    protected void onPreExecute() {
    }

    public static void finishLoading(boolean end) {
        isRequestedForExist = end;
    }
}