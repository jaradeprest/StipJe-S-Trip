package chiel.jara.stipjestrip;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Map;

import chiel.jara.stipjestrip.util.ComicHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView ivGif;
    private ProgressBar pbLoading;

    //OM TE TESTEN:
    private Button btnNext;


    //OM TE TESTEN:
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivGif = findViewById(R.id.iv_walkingman);
        pbLoading = findViewById(R.id.pb_loading);

        //OM TE TESTEN:
        btnNext=findViewById(R.id.btn_next);
        btnNext.setOnClickListener(nextListener);

        Glide.with(getApplicationContext()).load(R.drawable.marsupilami).into(ivGif);//imported glide dependencies in build.gradle
    }

}
