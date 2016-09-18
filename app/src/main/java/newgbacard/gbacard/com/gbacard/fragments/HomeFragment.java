package newgbacard.gbacard.com.gbacard.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.activities.BarcodeActivity;
import newgbacard.gbacard.com.gbacard.activities.EditContactActivity;
import newgbacard.gbacard.com.gbacard.activities.InviteFriendActivity;
import newgbacard.gbacard.com.gbacard.activities.SelectCardDesignActivity;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.utils.BarcodeUtil;
import newgbacard.gbacard.com.gbacard.utils.BluetoothUtil;
import newgbacard.gbacard.com.gbacard.utils.ConnectionDetector;
import newgbacard.gbacard.com.gbacard.utils.Constants;
import newgbacard.gbacard.com.gbacard.utils.EmailUtil;
import newgbacard.gbacard.com.gbacard.utils.MessageUtil;
import newgbacard.gbacard.com.gbacard.utils.Preferences;
import newgbacard.gbacard.com.gbacard.utils.SMSUtil;

/**
 * Created by Williamz on 13-Jul-16.
 */
public class HomeFragment extends Fragment {

    private String picturePath;
    private String storagePath;
    private String message;
    private String inviteOptions[];

    private Button sendButton;
    private Button editButton;

    private TextView textViewName;
    private TextView textViewPhone;
    private TextView textViewEmail;

    private ImageView barcodeImageView;
    //private ImageView userPhotoImageView;

    private ImageView card_rel_layout;
    Bundle bundle;
    Preferences pref;


    Boolean isInternetPresent = false;
    ConnectionDetector connectionDetector;

    private Contact userContact;

    private SMSUtil smsUtil;
    private EmailUtil emailUtil;
    private BluetoothUtil bluetoothUtil;
    private BarcodeUtil barcodeUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, null);

        setHasOptionsMenu(true);

        connectionDetector = new ConnectionDetector(this.getActivity().getApplicationContext());

        pref = new Preferences(getActivity());
        //pref.checkLogin();

        setUpViews(view);
        setUpListeners();

        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    storagePath = pref.getStoragePath();

                    picturePath = pref.getUserPicturePath();
                    /*Bitmap photoBitmap = (BitmapFactory.decodeFile(picturePath));
                    userPhotoImageView.setImageBitmap(photoBitmap);*/

                    userContact = new Contact();

                    userContact = pref.getUserDetails();

                    String firstName = userContact.getFirstName() != null ? userContact.getFirstName() : "";
                    String lastName = userContact.getLastName() != null ? userContact.getLastName() : "";
                    String email = userContact.getEmail() != null ? userContact.getEmail() : "";
                    String phoneNumber = userContact.getPhoneNumber() != null ? userContact.getPhoneNumber() : "";
                    String address = userContact.getAddress() != null ? userContact.getAddress() : "";

                    String fullName = firstName + " " + lastName;
                    Log.i(Constants.TAG_GBACARD, "Fullname: " + fullName);

                    textViewName.setText(fullName);
                    textViewPhone.setText(phoneNumber);
                    textViewEmail.setText(email);

                    Bitmap cardTemplateBitmap = pref.getCardTemplate();

                    if (cardTemplateBitmap != null){
                        card_rel_layout.setImageBitmap(cardTemplateBitmap);
                    }

                    else{
                        Drawable drawable = getResources().getDrawable(R.drawable.card_template_1);
                        card_rel_layout.setBackground(drawable);
                    }


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

                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void setUpViews(View view){
        textViewName = (TextView)view.findViewById(R.id.person_name);
        textViewPhone = (TextView)view.findViewById(R.id.person_phone);
        textViewEmail = (TextView)view.findViewById(R.id.email_person);
        sendButton = (Button)view.findViewById(R.id.sendButton);
        //editButton = (Button)view.findViewById(R.id.edit_icon);

        card_rel_layout = (ImageView)view.findViewById(R.id.card_background);


        //userPhotoImageView = (ImageView) view.findViewById(R.id.image);
        barcodeImageView = (ImageView) view.findViewById(R.id.imageView1);
    }

    public void setUpListeners(){
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //showOtherFieldsDialog();
                showExchangeDialog();
            }
        });

     /*   editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isInternetPresent = connectionDetector.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent);
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlert( "No Internet Connection",
                            "You don't have internet connection.");
                }
            }
        });*/

        barcodeImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                barcodeImageView.setVisibility(barcodeImageView.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                return false;
            }
        });
    }

    public void showExchangeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send Your Contact By");

        Resources res = getResources();
        inviteOptions = res.getStringArray(R.array.exchange_options);
        builder.setItems(R.array.exchange_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                if(inviteOptions[item].equalsIgnoreCase("QRCode")){
                   // Toast.makeText(getActivity(), "Touch to dismiss", Toast.LENGTH_LONG).show();
                   // barcodeUtil = new BarcodeUtil();
                   // barcodeUtil.showBarcode(getActivity(), bundle, barcodeImageView);

                    Intent intent = new Intent(getActivity(), BarcodeActivity.class);
                    startActivity(intent);
                }
                else if(inviteOptions[item].equalsIgnoreCase("SMS")){
                    smsUtil = new SMSUtil();
                    smsUtil.sendContactBySms(getActivity(), message);
                }
                else if(inviteOptions[item].equalsIgnoreCase("Bluetooth")){
                    bluetoothUtil = new BluetoothUtil();
                    bluetoothUtil.sendContactByBluetooth(getActivity(), storagePath);
                }
                else if(inviteOptions[item].equalsIgnoreCase("Email")){
                    String subject = userContact.getFirstName() + " " + userContact.getLastName();
                    emailUtil = new EmailUtil();
                    emailUtil.sendContactByEmail(getActivity(), storagePath, subject);
                }
                else if(inviteOptions[item].equalsIgnoreCase("Snap It")){
                    MessageUtil.showAlert(getActivity(), "Coming Soon", "This feature uses optical character recognition.");
                }
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_select_card_design) {
            Intent intent = new Intent(getActivity(), SelectCardDesignActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_edit_profile) {
            Intent intent = new Intent(getActivity(), EditContactActivity.class);
            intent.putExtra(Constants.TAG_CONTACT_DETAIL, userContact);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_invite_friend) {
            Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
            intent.putExtra(Constants.TAG_CONTACT_DETAIL, userContact);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}