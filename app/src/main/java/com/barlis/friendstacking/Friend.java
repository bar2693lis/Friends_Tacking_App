package com.barlis.friendstacking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Friend implements Serializable {

    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private String birthdayDate;
    private boolean isBestFriend;
    transient private Bitmap photo;
    private File file;

    public Friend(String name, String phoneNumber, String address, String email, String birthdayDate, boolean isBestFriend, File file) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.birthdayDate = birthdayDate;
        this.isBestFriend = isBestFriend;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public boolean isBestFriend() {
        return isBestFriend;
    }

    public void setBestFriend(boolean bestFriend) {
        isBestFriend = bestFriend;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        photo.compress(Bitmap.CompressFormat.JPEG, 50, out);
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        photo = BitmapFactory.decodeStream(in);
        in.defaultReadObject();
    }
}
