package com.reversecoder.ci.util;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.reversecoder.ci.R;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class FragmentUtilsManager {

    public static void changeFragment(Activity activity, android.app.Fragment targetFragment, String fragmentName) {
        activity.getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, fragmentName)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public static void changeSupportFragment(AppCompatActivity activity, android.support.v4.app.Fragment targetFragment, String fragmentName) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, fragmentName)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public android.app.Fragment getVisibleFragment(Activity activity, String fragmentTag) {
        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment fragment = (android.app.Fragment) fm.findFragmentByTag(fragmentTag);
        return fragment;
    }

    public android.app.Fragment getVisibleFragment(Activity activity, int containerResID) {
        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment fragment = (android.app.Fragment) fm.findFragmentById(containerResID);
        return fragment;
    }

    public Fragment getVisibleSupportFragment(AppCompatActivity activity, String fragmentTag) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentByTag(fragmentTag);
        return fragment;
    }

    public Fragment getVisibleSupportFragment(AppCompatActivity activity, int containerResID) {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentById(containerResID);
        return fragment;
    }
}
