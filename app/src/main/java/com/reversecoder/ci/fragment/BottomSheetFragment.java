package com.reversecoder.ci.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.ci.R;

import static com.reversecoder.ci.util.AllConstants.KEY_CONTACT_DETAIL;
import static com.reversecoder.ci.util.AppUtils.isSimSupport;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    Contact contact;
    BottomSheetBehavior behavior;
    View contentView;
    TextView tvName, tvNumber, tvSms, tvEmail;
    LinearLayout llCall, llSms, llEmail;
    ImageView ivCloseBottomSheet, ivProfilePicBottomSheet;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        contentView = View.inflate(getContext(), R.layout.bottom_sheet_contact_detail, null);
        dialog.setContentView(contentView);

        iniBottomSheetView();
        initBottomSheetAction();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void iniBottomSheetView() {
        Bundle args = getArguments();
        if ((args != null) && (getArguments().containsKey(KEY_CONTACT_DETAIL))) {
            contact = getArguments().getParcelable(KEY_CONTACT_DETAIL);
            contact = Contact.getResponseObject(getArguments().getString(KEY_CONTACT_DETAIL), Contact.class);
        }

        tvName = (TextView) contentView.findViewById(R.id.tv_bottom_sheet_name);
        tvNumber = (TextView) contentView.findViewById(R.id.tv_bottom_sheet_number);
        tvSms = (TextView) contentView.findViewById(R.id.tv_bottom_sheet_sms);
        tvEmail = (TextView) contentView.findViewById(R.id.tv_bottom_sheet_email);
        llCall = (LinearLayout) contentView.findViewById(R.id.ll_call);
        llSms = (LinearLayout) contentView.findViewById(R.id.ll_sms);
        llEmail = (LinearLayout) contentView.findViewById(R.id.ll_email);
        ivCloseBottomSheet = (ImageView) contentView.findViewById(R.id.iv_bottom_sheet_close);
        ivProfilePicBottomSheet = (ImageView) contentView.findViewById(R.id.iv_bottom_sheet_pic);

        if (contact != null) {
            tvName.setText(contact.getDisplayName());
            tvNumber.setText((contact.getPhoneNumbers().size() > 0) ? contact.getPhoneNumbers().get(0).getNumber() : "Contact has no number");
            tvSms.setText((contact.getPhoneNumbers().size() > 0) ? "Write SMS" : "Contact has no number");
            tvEmail.setText((contact.getEmails().size() > 0) ? contact.getEmails().get(0).getAddress() : "Contact has no email id");
            if (contact.getPhotoUri() != null) {
                ivProfilePicBottomSheet.setImageURI(Uri.parse(contact.getPhotoUri()));
//                File uriFile = new File(FilePathManager.getRealPath(getActivity(),Uri.parse(contact.getLargePhotoUri())));
//                Picasso.with(getActivity())
//                        .load(uriFile)
//                        .placeholder(R.drawable.icon_profile)
//                        .into(ivProfilePicBottomSheet);
            }
        }

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        behavior = (BottomSheetBehavior) params.getBehavior();
    }

    private void initBottomSheetAction() {
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            break;
                        case BottomSheetBehavior.STATE_HIDDEN:
                            dismiss();
                            break;
                        case BottomSheetBehavior.STATE_SETTLING:
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }

        llCall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!tvNumber.getText().toString().trim().equalsIgnoreCase(getString(R.string.toast_contact_has_no_number))) {
                    if (isSimSupport(getActivity())) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + tvNumber.getText().toString().trim()));
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.toast_your_sim_card_is_absent), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_contact_has_no_number), Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSms.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!tvNumber.getText().toString().trim().equalsIgnoreCase(getString(R.string.toast_contact_has_no_number))) {
                    if (isSimSupport(getActivity())) {
                        Intent callIntent = new Intent(Intent.ACTION_SENDTO);
                        callIntent.setData(Uri.parse("smsto:" + tvNumber.getText().toString().trim()));
                        startActivity(callIntent);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.toast_your_sim_card_is_absent), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_contact_has_no_number), Toast.LENGTH_SHORT).show();
                }
            }
        });

        llEmail.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!tvEmail.getText().toString().trim().equalsIgnoreCase(getString(R.string.toast_contact_has_no_email_id))) {
                    /***********************************
                     * Need to check internet connection
                     **********************************/
                    Intent callIntent = new Intent(Intent.ACTION_SENDTO);
                    callIntent.setData(Uri.parse("mailto:" + tvEmail.getText().toString().trim()));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.toast_contact_has_no_email_id), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showBottomSheet(BottomSheetBehavior behavior) {
        behavior.setHideable(false);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hideBottomSheet(BottomSheetBehavior behavior) {
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
