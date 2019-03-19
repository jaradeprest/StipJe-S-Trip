package chiel.jara.stipjestrip;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView ivGif;
    //private ComicHandler myComicHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivGif = findViewById(R.id.iv_walkingman);
        Glide.with(getApplicationContext()).load(R.drawable.marsupilami).into(ivGif);  //imported glide dependencies in build.gradle
    }

    private void downloadDate(){
        Thread backThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://bruxellesdata.opendatasoft.com/explore/dataset/comic-book-route/api/").get().build();
                    Response response = client.newCall(request).execute();

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
    }

}
