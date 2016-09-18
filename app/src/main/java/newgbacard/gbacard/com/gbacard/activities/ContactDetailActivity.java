package newgbacard.gbacard.com.gbacard.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import customfonts.MyTextView;
import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.utils.BluetoothUtil;
import newgbacard.gbacard.com.gbacard.utils.Constants;
import newgbacard.gbacard.com.gbacard.utils.ContactUtil;
import newgbacard.gbacard.com.gbacard.utils.EmailUtil;
import newgbacard.gbacard.com.gbacard.utils.PhoneUtil;
import newgbacard.gbacard.com.gbacard.utils.Preferences;
import newgbacard.gbacard.com.gbacard.utils.SMSUtil;

public class ContactDetailActivity extends AppCompatActivity {

    private MyTextView textViewFirstName;
    private MyTextView textViewLastName;
    private MyTextView textViewPhoneNumber;
    private MyTextView textViewEmail;
    private MyTextView textViewAddress;

    private LinearLayout callContact;
    private LinearLayout messageContact;
    private LinearLayout emailContact;

    private ImageView userPhotoImageView;

    private Preferences pref;

    private Contact userContact;
    private Bundle bundle;

    private String message;
    private String picturePath;
    private String sendingPhoneNumber;

    private SMSUtil smsUtil;
    private ContactUtil contactUtil;
    private BluetoothUtil bluetoothUtil;
    private PhoneUtil phoneUtil;
    private EmailUtil emailUtil;

    private Contact contact;

    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
        setUpListeners();

        intent = getIntent();

        if(intent != null){
            contact = (Contact)intent.getSerializableExtra(Constants.TAG_CONTACT_DETAIL);

            if(contact != null){
                if(intent.hasExtra(Constants.TAG_RECEIVE_AND_SEND)){
                    smsExchangeProcess(intent, contact);
                }else{
                    //fetchValues();
                    setContactDetails(contact);
                }
            }
        }
    }

    public void setUpViews(){
        textViewFirstName = (MyTextView)findViewById(R.id.contactDetail_firstName);
        textViewLastName = (MyTextView)findViewById(R.id.contactDetail_lastName);
        textViewPhoneNumber = (MyTextView)findViewById(R.id.contactDetail_phoneNumber);
        textViewEmail = (MyTextView)findViewById(R.id.contactDetail_email);
        textViewAddress = (MyTextView)findViewById(R.id.contactDetail_address);

        callContact = (LinearLayout)findViewById(R.id.callContact);
        messageContact = (LinearLayout)findViewById(R.id.messageContact);
        emailContact = (LinearLayout)findViewById(R.id.emailContact);

        userPhotoImageView = (ImageView)findViewById(R.id.userPhoto);

        pref = new Preferences(getApplicationContext());
    }

    public void setUpListeners(){
        callContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneUtil = new PhoneUtil();
                phoneUtil.callContact(ContactDetailActivity.this, textViewPhoneNumber.getText().toString());
            }
        });

        messageContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsUtil = new SMSUtil();
                smsUtil.sendSMSUsingPhoneNumber(ContactDetailActivity.this, textViewPhoneNumber.getText().toString());
            }
        });

        emailContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailUtil = new EmailUtil();
                emailUtil.sendPlainEmail(ContactDetailActivity.this, textViewEmail.getText().toString());
            }
        });
    }

    public void smsExchangeProcess(final Intent intent, final Contact contact){
        ContactDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contactUtil = new ContactUtil();

                /* name = intent.getStringExtra(Constants.TAG_NAME);
                phoneNumber = intent.getStringExtra(Constants.TAG_PHONE_NUMBER);
                email = intent.getStringExtra(Constants.TAG_EMAIL);
                address = intent.getStringExtra(Constants.TAG_ADDRESS);*/
                sendingPhoneNumber = intent.getStringExtra(Constants.TAG_SENDER_PHONE_NUMBER);

                contactUtil.createContact(ContactDetailActivity.this, contact);

                String fullName = contact.getFirstName() + " " + contact.getLastName();

                message = Constants.TAG_GBACARD_PREFIX
                        + "Name:" + fullName + ","
                        + "Phone:" + contact.getPhoneNumber() + ","
                        + "Email:" + contact.getEmail() + ","
                        + "Address:" + contact.getAddress();

                askToSendSMSBack(fullName, sendingPhoneNumber, message);
            }
        });
    }

    public void askToSendSMSBack(String fullName, final String senderPhoneNumber, final String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Exchange Contact");
        alert.setMessage("Do you want to send your contact to " + fullName + "?");
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                smsUtil.sendContactExchangeSMS(ContactDetailActivity.this, senderPhoneNumber, message);
            }
        });
        alert.show();
    }

    public void setContactDetails(final Contact contact) {
        ContactDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* picturePath = pref.getUserPicturePath();
                Bitmap photoBitmap = (BitmapFactory.decodeFile(picturePath));
                userPhotoImageView.setImageBitmap(photoBitmap);*/

                Bitmap bmp;

                try{
                    bmp = contact.getThumb();

                    if(bmp != null){
                        userPhotoImageView.setImageBitmap(bmp);
                    }
                    else{
                        userPhotoImageView.setImageResource(R.drawable.ic_user_icon_white);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                String firstName = contact.getFirstName() != null && !contact.getFirstName().equalsIgnoreCase("")
                        ? contact.getFirstName() : "";
                String lastName = contact.getLastName() != null && !contact.getLastName().equalsIgnoreCase("")
                        ? contact.getLastName() : "";
                String email = contact.getEmail() != null && !contact.getEmail().equalsIgnoreCase("")
                        ? contact.getEmail() : "N/A";
                String phoneNumber = contact.getPhoneNumber() != null && !contact.getPhoneNumber().equalsIgnoreCase("")
                        ? contact.getPhoneNumber() : "N/A";
                String address = contact.getAddress() != null && !contact.getAddress().equalsIgnoreCase("")
                        ? contact.getAddress() : "N/A";

                textViewFirstName.setText(firstName);
                textViewLastName.setText(lastName);
                textViewPhoneNumber.setText(phoneNumber);
                textViewEmail.setText(email);
                textViewAddress.setText(address);

                bundle = new Bundle();
                bundle.putString(ContactsContract.Intents.Insert.NAME, firstName + " " + lastName);
                bundle.putString(ContactsContract.Intents.Insert.EMAIL, email);
                bundle.putString(ContactsContract.Intents.Insert.PHONE, phoneNumber);
                bundle.putString(ContactsContract.Intents.Insert.PHONE, address);

                message = Constants.TAG_GBACARD_PREFIX
                        + "Name:" + firstName + " " + lastName + ","
                        + "Phone:" + phoneNumber + ","
                        + "Email:" + email + ","
                        + "Address:" + address;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {

        }

        else if(id == R.id.action_edit){
            Intent intent = new Intent(ContactDetailActivity.this, EditContactActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
