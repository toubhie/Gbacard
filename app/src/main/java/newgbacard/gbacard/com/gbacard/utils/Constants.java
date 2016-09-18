package newgbacard.gbacard.com.gbacard.utils;

import android.os.Environment;

/**
 * Created by Williamz on 13-Jul-16.
 */
public class Constants {
    //All QREncoder Constants
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BLACK = 0xFF000000;

    //All preferences constants
    // Shared pref mode
    public static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String SHARED_PREFERENCE_NAME = "GBACARD_SHARED_PREFERENCE";

    // All Shared Preferences Keys
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_FULL_NAME = "fullName";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_SALUTATION = "salutation";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_STORAGE_PATH = "storagePath";
    public static final String KEY_USER_PICTURE_PATH = "picturePath";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CARD_POSITION = "cardPosition";
    public static final String KEY_ENCODED_CARD_TEMPLATE = "encodedCardTemplate";
    public static final String KEY_USER_ID = "userId";

    //All Tags
    public static final String TAG_GBACARD = "GBACARD";
    public static final String TAG_GBACARD_PREFIX = "gbacard::";
    public static final String TAG_MIMETYPE_RADUTOKEN 	= "vnd.android.cursor.item/my_contact";
    public static final String TAG_STORAGE_PATH = "storage_path";
    public static final String TAG_FILE = "file";
    public static final String TAG_IMAGE_BYTE_ARRAY = "imageByteArray";
    public static final String TAG_FIRSTNAME = "firstName";
    public static final String TAG_LASTNAME = "lastName";
    public static final String TAG_PHONE_NUMBER = "phoneNumber";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_ADDRESS = "address";
    public static final String TAG_PASSWORD = "password";
    public static final String TAG_NAME = "name";
    public static final String TAG_SENDER_PHONE_NUMBER = "senderPhoneNumber";
    public static final String TAG_RECEIVE_AND_SEND = "receiveAndSend";
    public static final String TAG_CONTACT_DETAIL = "contactDetail";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_CODE = "code";
    public static final String TAG_USER_ID = "userId";

    public static final String TAG_SZ_TOKEN = String.format("MY_CONTACT", System.currentTimeMillis());

    //Parse Constants
    public static final String TAG_PARSE_NAME = "GBACARD_PARSE";
    public static final String KEY_PARSE_OBJECT_ID = "objectId";
    public static final String KEY_PARSE_FIRST_NAME = "firstName";
    public static final String KEY_PARSE_LAST_NAME = "lastName";
    public static final String KEY_PARSE_EMAIL = "email";
    public static final String KEY_PARSE_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_PARSE_ADDRESS = "address";
    public static final String KEY_PARSE_PASSWORD = "password";


    //Image Upload Selection
    public static final int TAKE_PICTURE = 1;
    public static final int ACTIVITY_SELECT_IMAGE = 2;

    //Select Country
    public static final int SELECT_COUNTRY = 3;

    public static String TAKE_PHOTO = "Take Photo";
    public static String CHOOSE_FROM_GALLERY = "Choose from Gallery";
    public static String CANCEL = "Cancel";

    //Images Path on SD Card
    public static final String GBACARD_PATH = "/Gbacard/";
    public static final String GBACARD_ABSOLUTE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + GBACARD_PATH;
    public static final String CAPTURED_IMAGES = "Captured Images/";
    public static final String CAPTURED_IMAGES_PATH = GBACARD_ABSOLUTE_PATH + CAPTURED_IMAGES;
    public static final String SAVED_IMAGE = "Saved Images/";
    public static final String SAVED_IMAGES_PATH = GBACARD_ABSOLUTE_PATH + SAVED_IMAGE;

    public static final String CONTACTS = "Contacts/";
    public static final String CONTACTS_PATH = GBACARD_ABSOLUTE_PATH + CONTACTS;

    //Bluetooth Tags
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 90;

    //Notification Tags
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    //All SMS Tags
    public static final String SENT = "sent";
    public static final String DELIVERED = "delivered";

    //All EndPoints
    public static final String BASE_ENDPOINT = "http://tracycharles.com/gbacard/";
    public static final String REGISTER_URL = BASE_ENDPOINT + "register.php";
    public static final String EDIT_PROFILE_URL = BASE_ENDPOINT + "edit_profile.php";
}
