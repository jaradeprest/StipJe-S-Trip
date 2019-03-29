package chiel.jara.stipjestrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import chiel.jara.stipjestrip.model.bar_model.Bar;
import chiel.jara.stipjestrip.model.bar_model.BarDatabase;
import chiel.jara.stipjestrip.model.comic_model.Comic;
import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;

/**
 * Created By Chiel&Jara 03/2019
 */

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvYear;
    private ImageView ivImage;
    private TextView tvAdres;
    private Comic chosenComic;
    private Bar chosenBar;
    private ImageButton btnRating;
    private TextView tvRating;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivImage=findViewById(R.id.iv_detail_image);
        tvTitle=findViewById(R.id.tv_title);
        tvAuthor=findViewById(R.id.tv_author);
        tvYear=findViewById(R.id.tv_year);
        tvAdres=findViewById(R.id.tv_adres);
        btnRating=findViewById(R.id.btn_rating);
        //rating van bars
        tvRating=findViewById(R.id.tv_adres);

        //for comic
        chosenComic= (Comic) getIntent().getSerializableExtra("comic");
        if(chosenComic != null) {
            //CHECK IF COMIC IS FAVORITE
            Log.i("is favorite?", String.valueOf(chosenComic.isFavorite()));
            if (!chosenComic.isFavorite()){
                btnRating.setImageResource(android.R.drawable.btn_star_big_off);
            }else {btnRating.setImageResource(android.R.drawable.btn_star_big_on);}

            tvTitle.setText(chosenComic.getName());
            tvAuthor.setText(chosenComic.getAuthor());
            tvYear.setText(chosenComic.getYear());
            //juiste afbeelding inladen
            try {
                FileInputStream fis = getApplicationContext().openFileInput(chosenComic.getImgID());
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                ivImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //BUTTON FAVORITE aan/uit zetten:
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!chosenComic.isFavorite()){
                        chosenComic.setFavorite(true);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateComic(chosenComic);
                        Log.i("update comic", String.valueOf(chosenComic.isFavorite()));
                    }else {
                        chosenComic.setFavorite(false);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_off);
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateComic(chosenComic);
                        Log.i("update comic", String.valueOf(chosenComic.isFavorite()));
                    }
                }
            });
            //GEOCODER OM ADRES TE KRIJGEN UIT COORDINATEN
            Geocoder comicLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses;
                addresses = (comicLocation.getFromLocation(chosenComic.getCoordinateLAT(), chosenComic.getCoordinateLONG(), 1));
                String comicAdres = addresses.get(0).getAddressLine(0);
                tvAdres.setText(comicAdres);
            } catch (IOException e) {
                e.printStackTrace();
                tvAdres.setText("No Adres");
            }
        }


        //for bar
        chosenBar = (Bar) getIntent().getSerializableExtra("bar");
        if(chosenBar != null) {
            //CHECK IF BAR IS RATED
            Log.i("is rated?", String.valueOf(chosenBar.isRated()));
            if (chosenBar.isRated()){
                btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                String stringRating = String.valueOf(chosenBar.getRating());
                tvRating.setText("Rating: "+ stringRating + "/10");
            }else {tvRating.setText("Give your rating by clicking on the star.");}


            tvTitle.setText(chosenBar.getName());
            String address = chosenBar.getStreet() + " " + chosenBar.getHouseNumber() + ", " + chosenBar.getPostalcode() + " " + chosenBar.getCity();
            String phoneWebsite = address + "\n" + chosenBar.getPhone() + "\n" + chosenBar.getWebsite();
            tvAuthor.setText(phoneWebsite);
            tvYear.setText(chosenBar.getDescription());
            //BUTTON RATING :
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRatingDialog(DetailActivity.this);
                    if (!chosenBar.isRated()){
                        chosenBar.setRated(true);
                        BarDatabase.getInstance(getApplicationContext()).getMethodsBar().updateBar(chosenBar);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                    }else{BarDatabase.getInstance(getApplicationContext()).getMethodsBar().updateBar(chosenBar);}
                }
            });
        }
    }

    private void showRatingDialog(Context context){
        final EditText rating = new EditText(context);
        rating.setInputType(InputType.TYPE_CLASS_NUMBER);
        rating.setRawInputType(Configuration.KEYBOARD_12KEY);
        final TextView onTen = new TextView(context);
        onTen.setText("/10");

        //creating a layout with those fields, to use in alertDialog
        //TODO create a layout in xml to put et en tv next to eachother
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 5;
        layout.addView(rating, params);
        layout.addView(onTen, params);

        AlertDialog ratingDialog = new AlertDialog.Builder(context)
                .setTitle("Give your rating")
                .setView(layout)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String yourRating = String.valueOf(rating.getText());
                        tvRating.setText("Rating: "+ yourRating + "/10");
                        chosenBar.setRating(Integer.valueOf(yourRating));
                        ImageButton btnRating = findViewById(R.id.btn_rating);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                        BarDatabase.getInstance(getApplicationContext()).getMethodsBar().updateBar(chosenBar);
                        //TODO save ratings per bar : calculate average ./10
                        //TODO make array allRatings. Add a rating to array (allRatings.add) and calculate value of rating = allRatings/ratings.size
                    }
                }).setNegativeButton("Cancel", null).create();
        ratingDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

//DOCUMENTATION: https://forums.xamarin.com/discussion/4573/back-button-restart-activity-instead-of-resuming-it