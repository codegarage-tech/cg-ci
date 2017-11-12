package com.reversecoder.ci.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.telephony.TelephonyManager;

import com.github.tamir7.contacts.Contact;
import com.reversecoder.ci.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class AppUtils {

//    public static String givenUnboundedRandomString() {
//        byte[] array = new byte[7]; // length is bounded by 7
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("UTF-8"));
//        return generatedString;
//    }
//
//    public static String givenBoundedRandomString() {
//        int leftLimit = 97; // letter 'a'
//        int rightLimit = 122; // letter 'z'
//        int targetStringLength = 10;
//        Random random = new Random();
//        StringBuilder buffer = new StringBuilder(targetStringLength);
//        for (int i = 0; i < targetStringLength; i++) {
//            int randomLimitedInt = leftLimit + (int)
//                    (random.nextFloat() * (rightLimit - leftLimit + 1));
//            buffer.append((char) randomLimitedInt);
//        }
//        String generatedString = buffer.toString();
//        return generatedString;
//    }

    /**
     * This method generates random string
     *
     * @return
     */
    public static String generateRandomString() {
        final String CHAR_LIST =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        final int RANDOM_STRING_LENGTH = 10;

        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    /**
     * This method generates random numbers
     *
     * @return int
     */
    public static int getRandomNumber() {
        final String CHAR_LIST =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    public static char[] getIndexList(ArrayList<Contact> list) {
        char[] result = new char[list.size()];
        int i = 0;
        for (Contact c : list) {
            result[i] = Character.toUpperCase(c.getDisplayName().charAt(0));
            i++;
        }
        return result;
    }

    public static int getColor(Context context, String key) {
        final Resources mRes = context.getResources();
        TypedArray mColors = mRes.obtainTypedArray(R.array.letter_tile_colors);
        final int color = Math.abs(key.hashCode()) % mColors.length();
        return mColors.getColor(color, Color.BLUE);
    }

    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);

    }

    public static boolean isNullOrEmpty(String myString) {
        return myString == null ? true : myString.length() == 0 || myString.equalsIgnoreCase("null") || myString.equalsIgnoreCase("");
    }

    public static String getTagName(Class<?> cls) {
        return cls.getSimpleName();
    }
}
