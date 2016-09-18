package newgbacard.gbacard.com.gbacard.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.models.Register;
import newgbacard.gbacard.com.gbacard.utils.ConnectionDetector;
import newgbacard.gbacard.com.gbacard.utils.Constants;
import newgbacard.gbacard.com.gbacard.utils.ContactUtil;
import newgbacard.gbacard.com.gbacard.utils.ConvertImage;
import newgbacard.gbacard.com.gbacard.utils.MessageUtil;
import newgbacard.gbacard.com.gbacard.utils.ParseUtil;
import newgbacard.gbacard.com.gbacard.utils.Preferences;
import newgbacard.gbacard.com.gbacard.webservices.WebService;

public class RegistrationActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText phoneNumber;
    private EditText repeatPassword;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText address;

    private FloatingActionButton registerButton;

    private ProgressDialog pDialog;

    private ImageView selectedCountryFlag;
    private ImageView userImage;

    private TextView selectedCountry;
    private TextView changeCountry;
    private TextView changeUserImage;

    private ConnectionDetector connectionDetector;

    private Preferences pref;

    private CharSequence[] options = {Constants.TAKE_PHOTO, Constants.CHOOSE_FROM_GALLERY, Constants.CANCEL};
    private String otherFieldsArray[];

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setUpViews();
        setUpListeners();

        connectionDetector = new ConnectionDetector(getApplicationContext());
        pref = new Preferences(getApplicationContext());
    }

    private void setUpViews() {
        password = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.repeatPassword);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        registerButton = (FloatingActionButton) findViewById(R.id.registerButton);
        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        address = (EditText) findViewById(R.id.address);

        selectedCountry = (TextView) findViewById(R.id.selectedCountry);
        changeCountry = (TextView) findViewById(R.id.changeCountry);
        changeUserImage = (TextView) findViewById(R.id.changeImage);

        selectedCountryFlag = (ImageView) findViewById(R.id.selectedCountryFlag);
        userImage = (ImageView) findViewById(R.id.userImage);
    }

    private void setUpListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isURLReachable(getApplicationContext())) {
                    if (valid()) {
                        showPreRegistrationDialog();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(v, "No Connection Found.", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showNotConnectedAlert();
                                }
                            })
                            .setActionTextColor(Color.RED);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }
        });

        changeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, CountrySelectionActivity.class);
                startActivityForResult(intent, Constants.SELECT_COUNTRY);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectImageOptions();
            }
        });

        changeUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectImageOptions();
            }
        });

    }

    private void register(){
        RegistrationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Contact contactPojo = new Contact();
                contactPojo.setFirstName(firstName.getText().toString());
                contactPojo.setLastName(lastName.getText().toString());
                contactPojo.setEmail(email.getText().toString());
                contactPojo.setPhoneNumber(phoneNumber.getText().toString());
                contactPojo.setAddress(address.getText().toString());
                contactPojo.setPassword(password.getText().toString());

                new RegistrationTask().execute(contactPojo);
            }
        });
    }

    public class RegistrationTask extends AsyncTask<Contact, Integer, Integer>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(RegistrationActivity.this, "Registering.", "Creating. Please wait...");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Contact... params) {
            Contact contactPojo = params[0];
            int result = 0;

            if (contactPojo != null){
                try {

                    ContactUtil contactUtil = new ContactUtil();
                    contactUtil.createContact(RegistrationActivity.this, contactPojo);

                    contactUtil.getVCardString(RegistrationActivity.this, contactPojo.getFirstName() + " " + contactPojo.getLastName());

                    ParseUtil parseUtil = new ParseUtil();
                    parseUtil.saveInParseOnline(RegistrationActivity.this, contactPojo);
                    parseUtil.saveInParseLocal(RegistrationActivity.this, contactPojo);

                    WebService webService = new WebService();
                    result = webService.registerUser(RegistrationActivity.this, contactPojo);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if(result == 0){
                MessageUtil.showAlert(RegistrationActivity.this, "An Error Occurred!", "Error Creating User.");
            }

            else if(result == 1){
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            else if(result == 2){
                MessageUtil.showAlert(RegistrationActivity.this, "Duplicate Phone Number!", "A User with this Phone Number already exists.");
            }
        }
    }

    private boolean valid() {
        boolean valid = true;
        /*if (!userName.getText().toString().trim().matches("[A-Za-z]+[_a-zA-Z0-9]{2,}")) {
            MessageUtil.showAlert(this, "Invalid Username!", "Username must contain 3 or more alphanumeric characters and/or underscores, starting with an alphabet");
            valid = false;
        } else*/ if (password.getText().toString().trim().isEmpty()) {
            MessageUtil.showAlert(this, "Invalid Password!", "Password cannot be empty");
            valid = false;
        } else if (!phoneNumber.getText().toString().trim().matches("(\\+[0-9]+[\\-\\.]*)?(\\([0-9]+\\)[\\- \\.]*)?([0-9][0-9\\- \\.]+[0-9])")) {
            MessageUtil.showAlert(this, "Invalid Phone Number!", "Please provide a valid phone number");
            valid = false;
        } else if (!password.getText().toString().trim().equals(repeatPassword.getText().toString())) {
            MessageUtil.showAlert(this, "Password Mismatch!", "Passwords don't match");
            valid = false;
        } else if (!email.getText().toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            MessageUtil.showAlert(this, "Invalid Email Address!", "Please provide a valid email address");
            valid = false;
        } else if (firstName.getText().toString().trim().length() < 2) {
            MessageUtil.showAlert(this, "Invalid First Name!", "Please provide a valid first name");
            valid = false;
        } else if (lastName.getText().toString().trim().length() < 2) {
            MessageUtil.showAlert(this, "Invalid Last Name!", "Please provide a valid last name");
            valid = false;
        }
        return valid;
    }

    public void showPreRegistrationDialog() {
        RegistrationActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are sure your details are correct?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                register();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void showNotConnectedAlert(){
        RegistrationActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("No Connection Found");
                builder.setMessage("Please check your connection and try again.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void showSelectImageOptions(){
        RegistrationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("Add Company Logo or Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (options[which].equals(Constants.TAKE_PHOTO)) {
                            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(captureIntent, Constants.TAKE_PICTURE);
                        } else if (options[which].equals(Constants.CHOOSE_FROM_GALLERY)) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, Constants.ACTIVITY_SELECT_IMAGE);
                        } else if (options[which].equals(Constants.CANCEL)) {
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == Constants.TAKE_PICTURE){
                chooseCamera(intent);
            }
            else if(requestCode == Constants.ACTIVITY_SELECT_IMAGE){
                chooseGallery(intent);
            }
            else if (requestCode == Constants.SELECT_COUNTRY){
                if (intent != null) {
                    String selectedCountry = intent.getStringExtra("country");
                    String[] g = selectedCountry.split(",");
                    Country tempCountry = new Country(CountrySelectionActivity.GetCountryZipCode(g[1]).trim(), g[1].trim());
                    if (country.equals(tempCountry)) {
                        return;
                    }
                    country = tempCountry;
                    displaySelectedCountry();
                }
            }

        }
    }

    private class Country {
        String name;
        String code;

        public Country(String name, String code) {
            this.name = name;
            this.code = code;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Country country = (Country) o;

            return !(code != null ? !code.equals(country.code) : country.code != null);

        }

        @Override
        public int hashCode() {
            return code != null ? code.hashCode() : 0;
        }
    }

    private Country country = new Country("Nigeria", "NG");

    void displaySelectedCountry() {
        selectedCountryFlag.setImageResource(getResources().getIdentifier("drawable/" + country.code.toLowerCase(), null, getPackageName()));
        selectedCountry.setText(country.name);
        phoneNumber.setText(String.format("+%d", phoneNumberUtil.getCountryCodeForRegion(country.code)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void chooseGallery(Intent intent){
        try {
            Uri selectedImage = intent.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            if (c != null) {
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Drawable drawable = new BitmapDrawable(thumbnail);
                userImage.setImageBitmap(thumbnail);

                //Save Image Path
                pref.saveUserPicturePath(picturePath);
                Log.i(Constants.TAG_GBACARD, "imagePath: " + pref.getUserPicturePath());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void chooseCamera(Intent intent){
        Bundle extras = intent.getExtras();
        Bitmap thePic = extras.getParcelable("data");
        String imgcurTime = dateFormat.format(new Date());
        File imageDirectory = new File(Constants.CAPTURED_IMAGES_PATH);
        imageDirectory.mkdirs();
        String picturePath = Constants.CAPTURED_IMAGES_PATH + imgcurTime + ".jpg";
        try {
            FileOutputStream out = new FileOutputStream(picturePath);
            if (thePic != null) {
                thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            out.close();

            ConvertImage convertImage = new ConvertImage();
            Bitmap thumbnail = convertImage.convertFileToBitmap(picturePath);
            userImage.setImageBitmap(thumbnail);

            //Save Image Path
            pref.saveUserPicturePath(picturePath);
            Log.i(Constants.TAG_GBACARD, "imagePath: " + pref.getUserPicturePath());
        } catch (FileNotFoundException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOtherFieldsDialog(){
        RegistrationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("Add Another Field");

                Resources res = getResources();
                otherFieldsArray = res.getStringArray(R.array.other_fields);

                builder.setItems(otherFieldsArray, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if (otherFieldsArray[item].equalsIgnoreCase("Address")){
                            Toast.makeText(getApplicationContext(), "Add Address", Toast.LENGTH_SHORT).show();
                        } else if (otherFieldsArray[item].equalsIgnoreCase("Organization")){
                            Toast.makeText(getApplicationContext(), "Add Organization", Toast.LENGTH_SHORT).show();
                        } else if (otherFieldsArray[item].equalsIgnoreCase("IM")){
                            Toast.makeText(getApplicationContext(), "Add IM", Toast.LENGTH_SHORT).show();
                        } else if (otherFieldsArray[item].equalsIgnoreCase("Nickname")) {
                            Toast.makeText(getApplicationContext(), "Add Nickname", Toast.LENGTH_SHORT).show();
                        } else if (otherFieldsArray[item].equalsIgnoreCase("Relationship")) {
                            Toast.makeText(getApplicationContext(), "Add Relationship", Toast.LENGTH_SHORT).show();
                        } else if (otherFieldsArray[item].equalsIgnoreCase("Notes")) {
                            Toast.makeText(getApplicationContext(), "Add Notes", Toast.LENGTH_SHORT).show();
                        } else if (otherFieldsArray[item].equalsIgnoreCase("Website")) {
                            Toast.makeText(getApplicationContext(), "Add Website", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
}
