package newgbacard.gbacard.com.gbacard.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.adapters.SettingsAdapter;
import newgbacard.gbacard.com.gbacard.models.Contact;
import newgbacard.gbacard.com.gbacard.models.Settings;
import newgbacard.gbacard.com.gbacard.utils.Constants;
import newgbacard.gbacard.com.gbacard.utils.DividerItemDecoration;
import newgbacard.gbacard.com.gbacard.utils.Preferences;

public class SettingsActivity extends AppCompatActivity {

    private List<Settings> settingsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SettingsAdapter mAdapter;

    private ProgressDialog pDialog;

    private Preferences pref;

    private Contact userContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpViews();
        setUpListeners();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        prepareSettingsData();
    }

    private void setUpViews(){
        pref = new Preferences(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new SettingsAdapter(settingsList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        userContact = new Contact();

        userContact = pref.getUserDetails();
    }

    private void setUpListeners(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(SettingsActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Settings setting = settingsList.get(position);

                if(setting.getSettingTitle().equalsIgnoreCase("About")){
                    Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                    startActivity(intent);
                }

                if(setting.getSettingTitle().equalsIgnoreCase("Change Card Template")){
                    Intent intent = new Intent(SettingsActivity.this, SelectCardDesignActivity.class);
                    startActivity(intent);
                }

                if(setting.getSettingTitle().equalsIgnoreCase("Edit Contact Detail")){
                    Intent intent = new Intent(SettingsActivity.this, EditContactActivity.class);
                    intent.putExtra(Constants.TAG_CONTACT_DETAIL, userContact);
                    startActivity(intent);
                }

                if(setting.getSettingTitle().equalsIgnoreCase("Invite a Friend")){
                    Intent intent = new Intent(SettingsActivity.this, InviteFriendActivity.class);
                    intent.putExtra(Constants.TAG_CONTACT_DETAIL, userContact);
                    startActivity(intent);
                }

                if(setting.getSettingTitle().equalsIgnoreCase("Sign Out")){
                    showDialog();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareSettingsData() {
        Settings setting = new Settings("About", getDrawable(SettingsActivity.this, R.drawable.ic_about));
        settingsList.add(setting);

        setting = new Settings("Change Card Template", getDrawable(SettingsActivity.this, R.drawable.ic_logout));
        settingsList.add(setting);

        setting = new Settings("Edit Contact Detail", getDrawable(SettingsActivity.this, R.drawable.ic_logout));
        settingsList.add(setting);

        setting = new Settings("Invite a Friend", getDrawable(SettingsActivity.this, R.drawable.ic_logout));
        settingsList.add(setting);

        setting = new Settings("Sign Out", getDrawable(SettingsActivity.this, R.drawable.ic_logout));
        settingsList.add(setting);

        mAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SettingsActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SettingsActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private void signOutUser(){
        new SignOutTask().execute();
    }

    public Drawable getDrawable(Context context, int imageId){
        return ContextCompat.getDrawable(context, imageId);
    }

    class SignOutTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SettingsActivity.this);
            pDialog.setMessage("Signing Out. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            try {
                // Clear all preferences
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();

                // exiting to Login screen
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }

    public void showDialog() {
        SettingsActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are sure you want to Sign Out?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                signOutUser();
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
}