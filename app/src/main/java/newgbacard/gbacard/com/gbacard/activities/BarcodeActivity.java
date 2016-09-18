package newgbacard.gbacard.com.gbacard.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.utils.BarcodeUtil;
import newgbacard.gbacard.com.gbacard.utils.Preferences;

public class BarcodeActivity extends AppCompatActivity {
    private ImageView barcodeImageView;

    private Bundle bundle;
    private Preferences pref;
    private Contact contact;

    private BarcodeUtil barcodeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
        setUpListeners();

        setUpBarcodeData();
    }

    public void setUpViews(){
        barcodeImageView = (ImageView)findViewById(R.id.barcode_imageView);

        pref = new Preferences(getApplicationContext());
    }

    public void setUpListeners(){

    }

    public void setUpBarcodeData(){
        try{
            contact = pref.getUserDetails();

            String firstName = contact.getFirstName() != null ? contact.getFirstName() : "";
            String lastName = contact.getLastName() != null ? contact.getLastName() : "";
            String email = contact.getEmail() != null ? contact.getEmail() : "";
            String phoneNumber = contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "";
            String address = contact.getAddress() != null ? contact.getAddress() : "";

            bundle = new Bundle();
            bundle.putString(ContactsContract.Intents.Insert.NAME, firstName + " " + lastName);
            bundle.putString(ContactsContract.Intents.Insert.EMAIL, email);
            bundle.putString(ContactsContract.Intents.Insert.PHONE, phoneNumber);
            bundle.putString(ContactsContract.Intents.Insert.PHONE, address);

            barcodeUtil = new BarcodeUtil();
            barcodeUtil.showBarcode(BarcodeActivity.this, bundle, barcodeImageView);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_scan) {
            scannerTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scannerTask(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);

                } catch (ActivityNotFoundException anfe) {
                    Log.e("onCreate", "Scanner Not Found", anfe);
                }

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                Toast toast = Toast.makeText(this, "Content: " + contents + " Format: " + format , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }

}
