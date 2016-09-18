package newgbacard.gbacard.com.gbacard.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.models.Register;

/**
 * Created by Williamz on 14-Jul-16.
 */
public class ParseUtil extends Application{
    private ParseObject parseObject;
    private Preferences pref;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "LinX843nmAXZEteLbp86juiXSJCNFuIWV7jLi00U", "BA57jf0Yq3v0D1Goo3YtevDT19Ttog8Ott09lqok");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void saveInParseOnline(final Context context, final Contact contact){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseObject = new ParseObject(Constants.TAG_PARSE_NAME);
                pref = new Preferences(context);

                try {
                    parseObject.put(Constants.KEY_PARSE_FIRST_NAME, contact.getFirstName());
                    parseObject.put(Constants.KEY_PARSE_LAST_NAME, contact.getLastName());
                    parseObject.put(Constants.KEY_PARSE_EMAIL, contact.getEmail());
                    parseObject.put(Constants.KEY_PARSE_PHONE_NUMBER, contact.getPhoneNumber());
                    parseObject.put(Constants.KEY_PARSE_ADDRESS, contact.getAddress());

                    parseObject.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                pref.saveParseObjectId(parseObject.getObjectId());

                            } else {
                                // The save failed.
                                Toast.makeText(context, "Saving Failed! ", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveInParseLocal(final Context context, final Contact contact){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parseObject = new ParseObject(Constants.TAG_PARSE_NAME);
                pref = new Preferences(context);

                try {
                    parseObject.put(Constants.KEY_PARSE_FIRST_NAME, contact.getFirstName());
                    parseObject.put(Constants.KEY_PARSE_LAST_NAME, contact.getLastName());
                    parseObject.put(Constants.KEY_PARSE_EMAIL, contact.getEmail());
                    parseObject.put(Constants.KEY_PARSE_PHONE_NUMBER, contact.getPhoneNumber());
                    parseObject.put(Constants.KEY_PARSE_ADDRESS, contact.getAddress());

                    parseObject.pinInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                pref.saveParseObjectId(parseObject.getObjectId());

                            } else {
                                // The save failed.
                                Toast.makeText(context, "Saving Failed! ", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateInParseOnline(final Context context, final Contact contact){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pref = new Preferences(context);

                ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.TAG_PARSE_NAME);
                query.whereEqualTo(Constants.KEY_PARSE_OBJECT_ID, pref.getParseObjectId());

                // Retrieve the object by id
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException parseException) {
                        if (parseException == null && objects.size() > 0) {
                            ParseObject parseObject = objects.get(0);
                            parseObject.put(Constants.KEY_PARSE_FIRST_NAME, contact.getFirstName());
                            parseObject.put(Constants.KEY_PARSE_LAST_NAME, contact.getLastName());
                            parseObject.put(Constants.KEY_PARSE_EMAIL, contact.getEmail());
                            parseObject.put(Constants.KEY_PARSE_PHONE_NUMBER, contact.getPhoneNumber());
                            parseObject.put(Constants.KEY_PARSE_ADDRESS, contact.getAddress());

                            parseObject.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //success, saved!
                                        Log.d(Constants.TAG_GBACARD, "Successfully saved!");
                                    } else {
                                        //fail to save!
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            //fail to get!!
                            parseException.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
