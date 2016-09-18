package newgbacard.gbacard.com.gbacard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.adapters.GalleryImageAdapter;
import newgbacard.gbacard.com.gbacard.utils.Constants;
import newgbacard.gbacard.com.gbacard.utils.Preferences;
import yuku.ambilwarna.AmbilWarnaDialog;

public class SelectCardDesignActivity extends AppCompatActivity {

    private ImageButton leftArrow;
    private ImageButton rightArrow;
    private Gallery gallery;
    private Button selectDesignButton;

    private RelativeLayout cardViewRelativeLayout;

    private int selectedImagePosition = 0;

    private List<Drawable> drawables;

    private GalleryImageAdapter galleryImageAdapter;

    private Preferences pref;

    private int color;

    private BitmapDrawable selectedBitmapDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card_design);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadCardDrawables();
        setUpViews();
        setUpListeners();
    }

    public void setUpViews(){
        cardViewRelativeLayout = (RelativeLayout) findViewById(R.id.cardView_rel_layout);
        leftArrow = (ImageButton) findViewById(R.id.left_arrow);
        rightArrow = (ImageButton) findViewById(R.id.right_arrow);
        gallery = (Gallery) findViewById(R.id.gallery);
        selectDesignButton = (Button) findViewById(R.id.selectDesign);

        pref = new Preferences(getApplicationContext());
    }

    public void setUpListeners(){
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImagePosition > 0) {
                    --selectedImagePosition;
                    Log.i(Constants.TAG_GBACARD, "Selected Image Position: " + selectedImagePosition);
                }
                gallery.setSelection(selectedImagePosition, false);
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImagePosition < drawables.size() - 1) {
                    ++selectedImagePosition;
                    Log.i(Constants.TAG_GBACARD, "Selected Image Position: " + selectedImagePosition);
                }
                gallery.setSelection(selectedImagePosition, false);

            }
        });

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                selectedImagePosition = pos;

                if (selectedImagePosition > 0 && selectedImagePosition < drawables.size() - 1) {

                    leftArrow.setEnabled(true);
                    rightArrow.setEnabled(true);

                    //leftArrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left_enabled));
                    //rightArrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right_enabled));

                } else if (selectedImagePosition == 0) {

                    leftArrow.setEnabled(false);
                    //leftArrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left_disabled));

                } else if (selectedImagePosition == drawables.size() - 1) {

                    rightArrow.setEnabled(false);
                    //rightArrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right_disabled));
                }

                Log.i(Constants.TAG_GBACARD, "Selected Image Position: " + selectedImagePosition);

                changeBorderForSelectedImage(selectedImagePosition);
                setSelectedImage(selectedImagePosition);
                //setOverallThemeColor(selectedImagePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        galleryImageAdapter = new GalleryImageAdapter(this, drawables);

        gallery.setAdapter(galleryImageAdapter);

        if (drawables.size() > 0) {
            gallery.setSelection(selectedImagePosition, false);
        }

        if (drawables.size() == 1) {
            rightArrow.setEnabled(false);
            //rightArrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right_disabled));
        }

        selectDesignButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              pref.saveCardTemplate(selectedBitmapDrawable);
            }
        });
    }

    private void changeBorderForSelectedImage(int selectedItemPos) {
        int count = gallery.getChildCount();

        for (int i = 0; i < count; i++) {

            ImageView imageView = (ImageView) gallery.getChildAt(i);
            imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_border));
            imageView.setPadding(3, 3, 3, 3);

        }

        ImageView imageView = (ImageView) gallery.getSelectedView();
        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_image_border));
        imageView.setPadding(3, 3, 3, 3);
    }

    public void loadCardDrawables() {
        drawables = new ArrayList<Drawable>();
        drawables.add(getResources().getDrawable(R.drawable.card_template_1));
        drawables.add(getResources().getDrawable(R.drawable.card_template_2));
        drawables.add(getResources().getDrawable(R.drawable.card_template_3));
        drawables.add(getResources().getDrawable(R.drawable.card_template_4));
        drawables.add(getResources().getDrawable(R.drawable.card_template_5));
        drawables.add(getResources().getDrawable(R.drawable.card_template_6));
        drawables.add(getResources().getDrawable(R.drawable.card_template_7));
        drawables.add(getResources().getDrawable(R.drawable.card_template_8));

    }

    public void setSelectedImage(int selectedImagePosition) {

        selectedBitmapDrawable = (BitmapDrawable) drawables.get(selectedImagePosition);
       // Drawable colo = colors.get(selectedImagePosition);
        Bitmap b = Bitmap.createScaledBitmap(selectedBitmapDrawable.getBitmap(), (int)
                (selectedBitmapDrawable.getIntrinsicHeight() * 0.9), (int) (selectedBitmapDrawable.getIntrinsicWidth() * 0.7), false);

       // Drawable d = new BitmapDrawable(getResources(), bitmap);

        cardViewRelativeLayout.setBackground(selectedBitmapDrawable);


        /*
        setButton.setBackground(colo);
        leftArrow.setBackground(colo);
        rightArrow.setBackground(colo);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_card_design_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_design) {
            try{
                pref.saveCardDesignTemplatePosition(selectedImagePosition);

                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectCardDesignActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            catch(Exception e){
                Toast.makeText(getApplicationContext(), "Not Saved!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        else if(id == R.id.action_open_color_dialog){
            openDialog(true);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                SelectCardDesignActivity.this.color = color;
                displayColor();
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void displayColor() {
    }
}