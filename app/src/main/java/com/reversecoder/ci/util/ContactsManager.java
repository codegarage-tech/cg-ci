package com.reversecoder.ci.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.reversecoder.ci.model.ContactInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ContactsManager {

    private static ContactsManager mInstance = null;
    private static Context mContext;

    public static ContactsManager getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new ContactsManager(context);
        }
        return mInstance;
    }

    private ContactsManager(Context context) {
        mContext = context;
    }

    public ArrayList<ContactInfo> getContacts() {
        ArrayList<ContactInfo> contacts = new ArrayList<ContactInfo>();
        try {
            ContactInfo contactInfo;
            final String[] projection = new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DELETED};
            final Cursor rawContacts = mContext.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, null, null, null);

            final int contactIdColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
            final int deletedColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.DELETED);

            if (rawContacts.moveToFirst()) {
                while (!rawContacts.isAfterLast()) {
                    final int contactId = rawContacts.getInt(contactIdColumnIndex);
                    final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

                    if (!deleted) {

                        int mContactId = contactId;
                        String mName = getName(contactId);
                        ArrayList<String> mPhoneNumbers = getPhoneNumbers(contactId);
                        ArrayList<String> mEmails = getEmails(contactId);
                        String mAddress = getAddress(contactId);
                        String mPhotoUriLarge = getPhotoUriLarge(contactId).toString();
                        String mPhotoUriThumbnail = getPhotoUriThumbnail(contactId).toString();
                        boolean mIsChecked = false;

                        contactInfo = new ContactInfo(mContactId, mName, mPhoneNumbers, mEmails, mAddress, mPhotoUriLarge, mPhotoUriThumbnail, mIsChecked);
                        contacts.add(contactInfo);
                    }
                    rawContacts.moveToNext();
                }
            }

            rawContacts.close();

            Collections.sort(contacts);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return contacts;
    }

    public String getName(int contactId) {
        String name = "";
        final String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

        final Cursor contact = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (contact.moveToFirst()) {
            name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.close();
        }
        contact.close();
        return name;

    }

    public ArrayList<String> getEmails(int contactId) {

        ArrayList<String> emails = new ArrayList<String>();
        String emailStr = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.DATA, // use
                // Email.ADDRESS
                // for API-Level
                // 11+
                ContactsContract.CommonDataKinds.Email.TYPE};

        final Cursor email = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (!email.isAfterLast()) {
                emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";";
                email.moveToNext();
            }
        }
        email.close();

        if (emailStr.length() > 0) {
            String mEmails[] = emailStr.split(";");
            for (int i = 0; i < mEmails.length; i++) {
                emails.add(mEmails[i]);
            }
        }

        return emails;

    }

    public Bitmap getPhoto(int contactId) {
        Bitmap photo = null;
        final String[] projection = new String[]{ContactsContract.Contacts.PHOTO_ID};

        final Cursor contact = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (contact.moveToFirst()) {
            final String photoId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            if (photoId != null) {
                photo = getBitmap(photoId);
            } else {
                photo = null;
            }
        }
        contact.close();

        return photo;
    }

    public Bitmap getBitmap(String photoId) {
        final Cursor photo = mContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, ContactsContract.Data._ID + "=?", new String[]{photoId}, null);

        final Bitmap photoBitmap;
        if (photo.moveToFirst()) {
            byte[] photoBlob = photo.getBlob(photo.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
            photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
        } else {
            photoBitmap = null;
        }
        photo.close();
        return photoBitmap;
    }

    public String getAddress(int contactId) {
        String postalData = "";
        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] addrWhereParams = new String[]{String.valueOf(contactId), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};

        Cursor addrCur = mContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);

        if (addrCur.moveToFirst()) {
            postalData = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }
        addrCur.close();
        return postalData;
    }

    public ArrayList<String> getPhoneNumbers(int contactId) {

        ArrayList<String> phoneNumbers = new ArrayList<String>();
        String phoneNumber = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE,};
        final Cursor phone = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);

            while (!phone.isAfterLast()) {
                phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex) + ";";
                phone.moveToNext();
            }

        }
        phone.close();

        if (phoneNumber.length() > 0) {
            String mPhoneNumbers[] = phoneNumber.split(";");
            for (int i = 0; i < mPhoneNumbers.length; i++) {
                phoneNumbers.add(mPhoneNumbers[i]);
            }
        }

        return phoneNumbers;
    }

    /********************
     * Photo
     *******************/
    public InputStream openThumbnailPhoto(int contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = mContext.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public InputStream openLargePhoto(int contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd =
                    mContext.getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
            return fd.createInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public Uri getPhotoUriThumbnail(int contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        return photoUri;
    }

    public Uri getPhotoUriLarge(int contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        return displayPhotoUri;
    }

//    private ArrayList<Contact> readContacts() {
//        Cursor cursor = null;
//        ArrayList<Contact> contacts = new ArrayList<Contact>();
//        Contact contact;
//        try {
////            cursor = getActivity().getContentResolver().query(
////                    ContactsContract.Data.CONTENT_URI,
////                    null,
////                    ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?",
////                    new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
////                    ContactsContract.Data.CONTACT_ID);
//
////            cursor = getActivity().getContentResolver().query(
////                    ContactsContract.Data.CONTENT_URI,
////                    null,
////                    ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?",
////                    new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
////                    ContactsContract.Data.CONTACT_ID);
//
//            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//                    String contact_ID = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
//                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                    String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    String contactEmail = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                    String contactLargePhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//                    String contactThumbnailPhotoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
//                    Log.d(TAG, "contactID: " + contactID);
//                    Log.d(TAG, "contact_ID: " + contact_ID);
//                    Log.d(TAG, "contact_large_photo_Uri: " + contactLargePhotoUri);
//                    Log.d(TAG, "contact_thumbnail_photo_Uri: " + contactThumbnailPhotoUri);
//                    contact = new Contact(contactName, contactNumber, contactEmail, contactLargePhotoUri, contactThumbnailPhotoUri);
//                    contacts.add(contact);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        //sort array data
//        Collections.sort(contacts, new Comparator<Contact>() {
//            @Override
//            public int compare(Contact o1, Contact o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//
//        return contacts;
//    }
}
