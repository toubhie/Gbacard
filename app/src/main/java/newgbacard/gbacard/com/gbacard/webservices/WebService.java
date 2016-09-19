package newgbacard.gbacard.com.gbacard.webservices;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.utils.Constants;
import newgbacard.gbacard.com.gbacard.utils.Preferences;

/**
 * Created by Williamz on 15-Aug-16.
 */
public class WebService {

    private JSONParser jsonParser = new JSONParser();

    private Preferences pref;

    public int registerUser(Context context, Contact contact){
        int result = 0;
        int userId;

        pref = new Preferences(context);

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_FIRSTNAME, contact.getFirstName()));
            params.add(new BasicNameValuePair(Constants.TAG_LASTNAME, contact.getLastName()));
            params.add(new BasicNameValuePair(Constants.TAG_EMAIL, contact.getEmail()));
            params.add(new BasicNameValuePair(Constants.TAG_PHONE_NUMBER, contact.getPhoneNumber()));
            params.add(new BasicNameValuePair(Constants.TAG_ADDRESS, contact.getAddress()));
            params.add(new BasicNameValuePair(Constants.TAG_PASSWORD, contact.getPassword()));

            JSONObject json = jsonParser.makeHttpRequest(Constants.REGISTER_URL, "POST", params);

            Log.d(Constants.TAG_GBACARD, json.toString());

            result = json.getInt(Constants.TAG_CODE);

            userId = json.getInt(Constants.TAG_USER_ID);

            pref.saveUserDetails(contact);
            pref.saveUserId(userId);

            return result;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int updateUserProfile(Context context, Contact contact){
        int result = 0;

        pref = new Preferences(context);

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_FIRSTNAME, contact.getFirstName()));
            params.add(new BasicNameValuePair(Constants.TAG_LASTNAME, contact.getLastName()));
            params.add(new BasicNameValuePair(Constants.TAG_EMAIL, contact.getEmail()));
            params.add(new BasicNameValuePair(Constants.TAG_PHONE_NUMBER, contact.getPhoneNumber()));
            params.add(new BasicNameValuePair(Constants.TAG_ADDRESS, contact.getAddress()));
            params.add(new BasicNameValuePair(Constants.TAG_USER_ID, String.valueOf(pref.getUserId())));

            JSONObject json = jsonParser.makeHttpRequest(Constants.EDIT_PROFILE_URL, "POST", params);

            Log.d(Constants.TAG_GBACARD, json.toString());

            result = json.getInt(Constants.TAG_CODE);

            pref.saveUserDetails(contact);

            return result;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
