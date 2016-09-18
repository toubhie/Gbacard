package newgbacard.gbacard.com.gbacard.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Williamz on 14-Jul-16.
 */

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Contact implements Serializable{
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String password;
    private Bitmap thumb;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

}
