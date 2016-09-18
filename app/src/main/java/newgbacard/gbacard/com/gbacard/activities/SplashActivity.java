package newgbacard.gbacard.com.gbacard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.utils.Preferences;

public class SplashActivity extends AppCompatActivity {

    private Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = new Preferences(getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity



                if (pref.isLoggedIn()) {

                    Intent intent= new Intent (SplashActivity.this,HomeActivity.class);
                    startActivity(intent);

                    // close this activity
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, RegistrationActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }
        }, 2000);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
}