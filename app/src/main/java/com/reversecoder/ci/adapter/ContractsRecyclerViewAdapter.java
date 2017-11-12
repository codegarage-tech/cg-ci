package com.reversecoder.ci.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.reversecoder.ci.R;
import com.reversecoder.ci.interfaces.TextGetter;
import com.reversecoder.ci.util.AppUtils;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ContractsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TextGetter {

    private Context context;
    private ArrayList<Contact> dataSet;

    // Constructor _________________________________________________________________________________
    public ContractsRecyclerViewAdapter(ArrayList<Contact> contacts, Context c) {
        this.dataSet = contacts;
        this.context = c;
    }

    // Callbacks ___________________________________________________________________________________
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_row_contact, parent, false);

        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ContactsViewHolder contactHolder = (ContactsViewHolder) holder;

        contactHolder.contactName.setText(dataSet.get(position).getDisplayName());
        contactHolder.contactNumber.setText((dataSet.get(position).getPhoneNumbers().size() > 0) ? dataSet.get(position).getPhoneNumbers().get(0).getNumber() : "");

        contactHolder.thumbnailImage.setVisibility(View.GONE);
        contactHolder.thumbnailText.setVisibility(View.VISIBLE);
        contactHolder.thumbnailText.setText(String.valueOf(dataSet.get(position).getDisplayName().charAt(0)).toUpperCase());
        GradientDrawable drawable = (GradientDrawable) contactHolder.thumbnailText.getBackground();
        drawable.setColor(AppUtils.getColor(context, dataSet.get(position).getDisplayName()));

//        if (dataSet.get(position).getPhotoUriThumbnail() != null) {
//            contactHolder.thumbnailText.setVisibility(View.GONE);
//            contactHolder.thumbnailImage.setVisibility(View.VISIBLE);
//            contactHolder.thumbnailImage.setImageURI(Uri.parse(dataSet.get(position).getPhotoUriThumbnail()));
//        }else{
//            contactHolder.thumbnailImage.setVisibility(View.GONE);
//            contactHolder.thumbnailText.setVisibility(View.VISIBLE);
//            contactHolder.thumbnailText.setText(String.valueOf(dataSet.get(position).getName().charAt(0)).toUpperCase());
//            GradientDrawable drawable = (GradientDrawable) contactHolder.thumbnailText.getBackground();
//            drawable.setColor(AppUtils.getColor(context, dataSet.get(position).getName()));
//        }

        if (dataSet.get(position).getDisplayName() != null) {
//            contactHolder.firstLetter.setVisibility(TextView.INVISIBLE);

//            new AsyncTask<Uri, Integer, Bitmap>() {
//                @Override
//                protected Bitmap doInBackground(Uri... params) {
//                    try {
//                        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), params[0]);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        return null;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap b) {
//                    contactHolder.thumbnail.setImageBitmap(b);
//                }
//            }.execute(dataSet.get(position).getThumbnail());
//
//            contactHolder.thumbnail.setImageURI(dataSet.get(position).getThumbnail());
        } else {
//            contactHolder.firstLetter.setVisibility(TextView.VISIBLE);
//            contactHolder.thumbnail.setImageResource(R.drawable.circle_icon);

//            GradientDrawable drawable = (GradientDrawable) contactHolder.thumbnail.getDrawable();
//            Random rnd = new Random();
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//            drawable.setColor(color);
        }

//        setRegularLineLayout(contactHolder);
    }


    // Getters and Setters _________________________________________________________________________
//    private void setFirstLineTextLayout(ContactsViewHolder vh) {
//        vh.firstLetter.setText("Set up my profile");
//        vh.firstLetter.setTextColor(Color.parseColor("#000000"));
//        vh.firstLetter.setTextSize(18);
//        vh.contactName.setText("");
//        vh.thumbnail.setVisibility(CircularImageView.INVISIBLE);
//    }

//    private void setRegularLineLayout(ContactsViewHolder vh) {
//        vh.firstLetter.setTextColor(Color.parseColor("#ffffff"));
//        vh.firstLetter.setTextSize(26);
//    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public Contact getContact(int pos) {
        return dataSet.get(pos);
    }

    public Contact getContactByName(String name) {
        for (Contact c : dataSet) {
            if (name.equals(c.getDisplayName())) {
                return c;
            }
        }

        return null;
    }

    public ArrayList<Contact> getDataSet() {
        return dataSet;
    }

    // Bubbler Filler ______________________________________________________________________________
    @Override
    public String getTextFromAdapter(int pos) {
        return String.valueOf(dataSet.get(pos).getDisplayName().charAt(0)).toUpperCase();
    }

    // ViewHolder class ____________________________________________________________________________
    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        //        TextView firstLetter;
        TextView contactName;
        TextView contactNumber;
        //        CircularImageView thumbnail;
        TextView thumbnailText;
        ImageView thumbnailImage;

        public ContactsViewHolder(View v) {
            super(v);
//            firstLetter = (TextView) v.findViewById(R.id.contact_first_letter);
            contactName = (TextView) v.findViewById(R.id.contact_name);
            contactNumber = (TextView) v.findViewById(R.id.contact_number);
            thumbnailText = (TextView) v.findViewById(R.id.tv_contact_thumbnail);
            thumbnailImage = (ImageView) v.findViewById(R.id.iv_contact_thumbnail);
        }
    }
}
