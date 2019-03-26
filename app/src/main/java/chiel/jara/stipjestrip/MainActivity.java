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

import chiel.jara.stipjestrip.util.bar_util.BarHandler;
import chiel.jara.stipjestrip.util.comic_util.ComicHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created By Chiel&Jara 03/2019
 */

public class MainActivity extends AppCompatActivity {

    private ImageView ivGif;
    private ProgressBar pbLoading;
    private ComicHandler myComicHandler;
    private BarHandler myBarHandler;

    //OM TE TESTEN:
    private Button btnNext;
    private Button btnMaps;


    //OM TE TESTEN:
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BarListActivity.class);
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
        ivGif = findViewById(R.id.iv_walkingman);
        pbLoading = findViewById(R.id.pb_loading);

        //OM TE TESTEN:
        btnNext=findViewById(R.id.btn_next);
        btnNext.setOnClickListener(nextListener);
        btnMaps=findViewById(R.id.btn_maps);
        btnMaps.setOnClickListener(mapsListener);

        myComicHandler = new ComicHandler( getApplicationContext());
        myBarHandler = new BarHandler(getApplicationContext());
        Glide.with(getApplicationContext()).load(R.drawable.marsupilami).into(ivGif);//imported glide dependencies in build.gradle
        downloadData();
    }

    private void downloadData(){
        Thread backThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://bruxellesdata.opendatasoft.com/api/records/1.0/search/?dataset=comic-book-route&rows=50").get().build();
                    Response response = client.newCall(request).execute();
                    OkHttpClient bars = new OkHttpClient();
                    Request barRequest = new Request.Builder().url("https://opendata.visitflanders.org/tourist/reca/beer_bars.json").get().build();
                    Response barResponse = bars.newCall(barRequest).execute();

                    if (barResponse.body() != null){
                        String barResponseBody = barResponse.body().string();
                        Message barMessage = new Message();
                        barMessage.obj = barResponseBody;
                        myBarHandler.sendMessage(barMessage);
                    }

                    if (response.body() != null){
                        String responsebodyText = response.body().string();
                        Message message = new Message();
                        message.obj = responsebodyText;
                        //STRING DOORSTUREN NAAR HANDLER
                        myComicHandler.sendMessage(message);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        backThread.start();
        //TODO als data is binnengehaald, dan wordt launchscreen gesloten en wordt map_activity getoond
    }

}
