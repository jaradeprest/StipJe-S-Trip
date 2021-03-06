package chiel.jara.stipjestrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import chiel.jara.stipjestrip.model.bar_model.Bar;
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
    private ImageButton btnVisited;
    private ImageButton btnMaps;

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
        btnVisited=findViewById(R.id.btn_detail_visited);
        btnMaps=findViewById(R.id.btn_detail_maps);
        //rating van bars
        tvRating=findViewById(R.id.tv_adres);

        //for comic
        chosenComic= (Comic) getIntent().getSerializableExtra("comic");
        if(chosenComic != null) {
            //CHECK IF COMIC IS FAVORITE
            if (!chosenComic.isFavorite()){
                btnRating.setImageResource(R.drawable.like);
                btnRating.setColorFilter(Color.rgb(127,127,127));
            }else {btnRating.setImageResource(R.drawable.like);
                btnRating.setColorFilter(Color.RED);
            }
            //chekc if comic is visited
            if (chosenComic != null){
                if (chosenComic.isVisited()){
                    btnVisited.setColorFilter(Color.WHITE);
                }else{btnVisited.setColorFilter(Color.rgb(127,127,127));}
            }
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
                        btnRating.setImageResource(R.drawable.like);
                        btnRating.setColorFilter(Color.RED);
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateComic(chosenComic);
                    }else {
                        chosenComic.setFavorite(false);
                        btnRating.setImageResource(R.drawable.like);
                        btnRating.setColorFilter(Color.rgb(127,127,127));
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateComic(chosenComic);
                    }
                }
            });
            //BUTTON VISITED aan/uit zetten:
            btnVisited.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!chosenComic.isVisited()){
                        chosenComic.setVisited(true);
                        btnVisited.setColorFilter(Color.WHITE);
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateComic(chosenComic);
                    }else{
                        chosenComic.setVisited(false);
                        btnVisited.setColorFilter(Color.rgb(127,127,127));
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateComic(chosenComic);
                    }
                }
            });
            //Listener to get from detailpage to map
            btnMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("chosen", chosenComic);
                    context.startActivity(intent);
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
            tvTitle.setText(chosenBar.getName());
            String address = chosenBar.getStreet() + " " + chosenBar.getHouseNumber() + ", " + chosenBar.getPostalcode() + " " + chosenBar.getCity();
            String phoneWebsite = address + "\n" + chosenBar.getPhone() + "\n" + chosenBar.getWebsite();
            tvAuthor.setText(phoneWebsite);
            tvYear.setText(chosenBar.getDescription());
            btnVisited.setVisibility(View.INVISIBLE);
            Glide.with(this).load(R.drawable.tap).into(ivImage);
            btnRating.setPadding(5,5,5,8);

            //CHECK IF BAR IS RATED
            if (chosenBar.isRated()){
                btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                String stringRating = String.valueOf(chosenBar.getRating());
                tvRating.setText("Rating: "+ stringRating + "/10");
            }else {tvRating.setText("Give your rating by clicking on the star.");
                btnRating.setImageResource(android.R.drawable.btn_star_big_off);
            }
            //BUTTON RATING :
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRatingDialog(DetailActivity.this);
                    if (!chosenBar.isRated()){
                        ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateBar(chosenBar);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                    }else{ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateBar(chosenBar);}
                }
            });
            //Listener to get from detailpage to map
            btnMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("chosenBar", chosenBar);
                    context.startActivity(intent);
                }
            });
        }
    }

    //creating an alerdialog to give your rating to the bar
    private void showRatingDialog(final Context context){
        //created a layout to use in alertDialog
        View ratingView = getLayoutInflater().inflate(R.layout.rating_window_bar, null, false);
        final EditText rating;
        rating = ratingView.findViewById(R.id.et_rating_alert_yourRating);

        AlertDialog ratingDialog = new AlertDialog.Builder(context)
                .setView(ratingView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String yourRating = String.valueOf(rating.getText());
                        int numberRating = Integer.valueOf(yourRating);
                        if (numberRating<=10){
                            tvRating.setText("Rating: "+ yourRating + "/10");
                            chosenBar.setRating(Integer.valueOf(yourRating));
                            ImageButton btnRating = findViewById(R.id.btn_rating);
                            btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                            chosenBar.setRated(true);
                            ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().updateBar(chosenBar);
                        }else {
                            Toast.makeText(getApplicationContext(), "Please enter a number between 1 and 10", Toast.LENGTH_LONG).show();
                            btnRating.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!chosenBar.isRated()) {
                            btnRating.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    }
                }).create();
        ratingDialog.show();
    }
}

//DOCUMENTATION: https://forums.xamarin.com/discussion/4573/back-button-restart-activity-instead-of-resuming-it