package com.reversecoder.ci.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.reversecoder.ci.R;
import com.reversecoder.ci.activity.HomeActivity;
import com.reversecoder.residemenu.ResideMenu;

import static com.reversecoder.ci.util.AllConstants.ASSET_FILE_TERMS_AND_CONDITIONS;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class TermsAndConditionsFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private PDFView pdfView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_terms_and_condition, container, false);
        setUpViews();

        return parentView;
    }

    private void setUpViews() {
        HomeActivity parentActivity = (HomeActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        ((HomeActivity) getActivity()).setTitle(getString(R.string.title_fragment_terms_and_condition));

        pdfView = (PDFView) parentView.findViewById(R.id.pdfView);
        pdfView.fromAsset(ASSET_FILE_TERMS_AND_CONDITIONS)
                .enableSwipe(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();
    }
}
