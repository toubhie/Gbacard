package newgbacard.gbacard.com.gbacard.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.adapters.ViewPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Home");

        HomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.addTab(tabLayout.newTab().setText("HOME"));
                tabLayout.addTab(tabLayout.newTab().setText("ADDRESS BOOK"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FAFAFA"));

                final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
                final ViewPagerAdapter adapter = new ViewPagerAdapter
                        (getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_select_card_design) {
            Intent intent = new Intent(HomeActivity.this, SelectCardDesignActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}