package com.reversecoder.ci.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reversecoder.ci.R;
import com.reversecoder.ci.model.SpinnerItem;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class UserSpinnerAdapter<T> extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<T> mData;
    private static LayoutInflater inflater = null;
    private ADAPTER_TYPE mAdapterType;

    public UserSpinnerAdapter(Activity activity, ADAPTER_TYPE adapterType) {
        mActivity = activity;
        mAdapterType = adapterType;
        mData = new ArrayList<T>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<T> getData() {
        return mData;
    }

    public void setData(ArrayList<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public int getItemPosition(String name) {
        for (int i = 0; i < mData.size(); i++) {
            if (((SpinnerItem) mData.get(i)).getName().contains(name)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.spinner_row_user, null);

        SpinnerItem mGender = (SpinnerItem) getItem(position);
//        if (mAdapterType == ADAPTER_TYPE.GENDER) {
//        }

        TextView names = (TextView) vi.findViewById(R.id.tv_gender);
        names.setText(mGender.getName());

        return vi;
    }

    public enum ADAPTER_TYPE {GENDER, CITY, COUNTRY}
}