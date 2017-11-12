package com.reversecoder.ci.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Md. Rashadul Alam
 *         Email: rashed.droid@gmail.com
 */
public class ContactInfo implements Parcelable, Comparable<ContactInfo> {

    private int contactId;
    private String name;
    private ArrayList<String> phoneNumbers;
    private ArrayList<String> emails;
    private String address;
    private String photoUriLarge;
    private String photoUriThumbnail;
    private boolean isChecked;
//    private Address address;

    public ContactInfo(int contactId, String name, ArrayList<String> phoneNumbers, ArrayList<String> emails, String address, String photoUriLarge, String photoUriThumbnail, boolean isChecked) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.emails = emails;
        this.address = address;
        this.photoUriLarge = photoUriLarge;
        this.photoUriThumbnail = photoUriThumbnail;
        this.isChecked = isChecked;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUriLarge() {
        return photoUriLarge;
    }

    public void setPhotoUriLarge(String photoUriLarge) {
        this.photoUriLarge = photoUriLarge;
    }

    public String getPhotoUriThumbnail() {
        return photoUriThumbnail;
    }

    public void setPhotoUriThumbnail(String photoUriThumbnail) {
        this.photoUriThumbnail = photoUriThumbnail;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    //Parcelable implementation
    public ContactInfo(Parcel parcel) {
        this.contactId = parcel.readInt();
        this.name = parcel.readString();
        this.phoneNumbers = parcel.readArrayList(String.class.getClassLoader());
        this.emails = parcel.readArrayList(String.class.getClassLoader());
        this.address = parcel.readString();
        this.photoUriLarge = parcel.readString();
        this.photoUriThumbnail = parcel.readString();
        this.isChecked = parcel.readInt() == 1;

//        address = (Address) in.readParcelable(Address.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.contactId);
        dest.writeString(this.name);
        dest.writeList(this.phoneNumbers);
        dest.writeList(this.emails);
        dest.writeString(this.address);
        dest.writeString(this.photoUriLarge);
        dest.writeString(this.photoUriThumbnail);
        dest.writeInt(this.isChecked ? 1 : 0);

        // Add inner class
//        dest.writeParcelable(address, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ContactInfo createFromParcel(Parcel in) {
            return new ContactInfo(in);
        }

        public com.reversecoder.ci.model.ContactInfo[] newArray(int size) {
            return new ContactInfo[size];
        }
    };

    @Override
    public int compareTo(ContactInfo another) {
        return this.getName().compareToIgnoreCase(another.getName());
    }
}