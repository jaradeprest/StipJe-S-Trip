package chiel.jara.stipjestrip;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import chiel.jara.stipjestrip.model.bar_model.Bar;
import chiel.jara.stipjestrip.model.comic_model.Comic;

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
    ImageButton btnRating;

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


        //for comic
        chosenComic= (Comic) getIntent().getSerializableExtra("comic");
        if(chosenComic != null) {
            tvTitle.setText(chosenComic.getName());
            tvAuthor.setText(chosenComic.getAuthor());
            tvYear.setText(chosenComic.getYear());

            try {
                FileInputStream fis = getApplicationContext().openFileInput(chosenComic.getImgID());
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                ivImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //BUTTON FAVORITE :
            //TODO WERKT NIET ALS JE SCHERM VERLAAT EN OPNIEUW OPENT ?? WHY ??
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chosenComic.isFavorite() == true){
                        chosenComic.setFavorite(false);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_off);
                    }else {
                        chosenComic.setFavorite(true);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                    }
                }
            });
            //CHECK IF COMIC IS FAVORITE
            if (chosenComic.isFavorite()==true){
                btnRating.setImageResource(android.R.drawable.btn_star_big_on);
            }else {btnRating.setImageResource(android.R.drawable.btn_star_big_off);}

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
            //BUTTON RATING :
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRatingDialog(DetailActivity.this);
                }
            });
        }
    }

    private void showRatingDialog(Context context){
        final EditText rating = new EditText(context);
        AlertDialog ratingDialog = new AlertDialog.Builder(context)
                .setTitle("Give your rating")
                .setView(rating)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String yourRating = String.valueOf(rating.getText());
                        TextView tvRating = findViewById(R.id.tv_adres);
                        tvRating.setText(yourRating);
                        ImageButton btnRating = findViewById(R.id.btn_rating);
                        btnRating.setImageResource(android.R.drawable.btn_star_big_on);
                        //TODO save ratings per bar : calculate average ./10
                    }
                }).setNegativeButton("Cancel", null).create();
        ratingDialog.show();
    }
}