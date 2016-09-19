package newgbacard.gbacard.com.gbacard.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.util.HashMap;

import newgbacard.gbacard.com.gbacard.activities.LoginActivity;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.models.Register;

/**
 * Created by Williamz on 13-Jul-16.
 */
public class Preferences {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    private ConvertImage convertImage;

    // Context
    Context _context;

    // Constructor
    public Preferences(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Constants.PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void saveUserDetails(Contact contact){
        // Storing login value as TRUE
        editor.putBoolean(Constants.IS_LOGIN, true);

        // Storing name in pref
        editor.putString(Constants.KEY_FIRST_NAME, contact.getFirstName());

        editor.putString(Constants.KEY_LAST_NAME, contact.getLastName());

        // Storing phone in pref
        editor.putString(Constants.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        editor.putString(Constants.KEY_EMAIL, contact.getEmail());

        editor.putString(Constants.KEY_ADDRESS, contact.getAddress());

        // commit changes
        editor.commit();
    }

    public Contact getUserDetails(){
        Contact contact = new Contact();

        contact.setFirstName(pref.getString(Constants.KEY_FIRST_NAME, null));
        contact.setLastName(pref.getString(Constants.KEY_LAST_NAME, null));
        contact.setPhoneNumber(pref.getString(Constants.KEY_PHONE_NUMBER, null));
        contact.setEmail(pref.getString(Constants.KEY_EMAIL, null));
        contact.setAddress(pref.getString(Constants.KEY_ADDRESS, null));

        return contact;
    }

    public void saveStoragePath(String storagePath){
        editor.putString(Constants.KEY_STORAGE_PATH, storagePath);

        editor.commit();
    }

    public String getStoragePath(){
        return pref.getString(Constants.KEY_STORAGE_PATH, null);
    }

    public void saveUserPicturePath(String picturePath){
        editor.putString(Constants.KEY_USER_PICTURE_PATH, picturePath);

        editor.commit();
    }

    public String getUserPicturePath(){
        return pref.getString(Constants.KEY_USER_PICTURE_PATH, null);
    }

    public void saveParseObjectId(String objectId){
        editor.putString(Constants.KEY_PARSE_OBJECT_ID, objectId);

        editor.commit();
    }

    public String getParseObjectId(){
        return pref.getString(Constants.KEY_PARSE_OBJECT_ID, null);
    }

    public void saveUserId(int userId){
        editor.putInt(Constants.KEY_USER_ID, userId);

        editor.commit();
    }

    public int getUserId(){
        return pref.getInt(Constants.KEY_USER_ID, 0);
    }


    public void saveCardDesignTemplatePosition(int cardPosition){
        editor.putInt(Constants.KEY_CARD_POSITION, cardPosition);

        editor.commit();
    }

    public int getCardDesignTemplatePosition(){
        return pref.getInt(Constants.KEY_CARD_POSITION, 1);
    }

    public void saveCardTemplate(Bitmap bitmap){
        try {
            convertImage = new ConvertImage();

            String encodedImage = convertImage.BitmapToBase64(bitmap);

            editor.putString(Constants.KEY_ENCODED_CARD_TEMPLATE, encodedImage);
            editor.commit();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getCardTemplate(){
        Bitmap bitmap = null;
        try {
            convertImage = new ConvertImage();

            String encodedImage = pref.getString(Constants.KEY_ENCODED_CARD_TEMPLATE, null);

            bitmap = convertImage.base64ToBitmap(encodedImage);

        } catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login,kjk
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(Constants.IS_LOGIN, false);
    }

}
