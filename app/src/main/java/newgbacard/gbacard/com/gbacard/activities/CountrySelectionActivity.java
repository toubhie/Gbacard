package newgbacard.gbacard.com.gbacard.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.utils.BaseActivity;

public class CountrySelectionActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] recourseList = getResources().getStringArray(R.array.CountryCodes);

        final ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(new CountriesListAdapter(this, recourseList));
        listview.setOnItemClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("country", (String) parent.getItemAtPosition(position));
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    static class CountriesListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CountriesListAdapter(Context context, String[] values) {
            super(context, R.layout.country_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.country_list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.txtViewCountryName);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imgViewFlag);

            String[] g = values[position].split(",");
            textView.setText(GetCountryZipCode(g[1]).trim());

            String pngName = g[1].trim().toLowerCase();
            imageView.setImageResource(context.getResources().getIdentifier("drawable/" + pngName, null, context.getPackageName()));
            return rowView;
        }


    }

    public static String GetCountryZipCode(String ssid) {
        Locale loc = new Locale("", ssid);
        return loc.getDisplayCountry().trim();
    }
}
