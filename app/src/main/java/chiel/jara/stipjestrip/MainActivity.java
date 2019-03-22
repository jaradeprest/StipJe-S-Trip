package chiel.jara.stipjestrip;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import chiel.jara.stipjestrip.util.ComicHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView ivGif;

    //OM TE TESTEN:
    private Button btnNext;
    private Button btnMaps;


    //OM TE TESTEN:
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
    private View.OnClickListener mapsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TRY OUT (start Listactivity vanuit main)


        ivGif = findViewById(R.id.iv_walkingman);

        //OM TE TESTEN:
        btnNext=findViewById(R.id.btn_next);
        btnNext.setOnClickListener(nextListener);
        btnMaps=findViewById(R.id.btn_maps);
        btnMaps.setOnClickListener(mapsListener);

        Glide.with(getApplicationContext()).load(R.drawable.marsupilami).into(ivGif);//imported glide dependencies in build.gradle
    }



}
