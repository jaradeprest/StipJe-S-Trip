package chiel.jara.stipjestrip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;


import com.squareup.picasso.Picasso;

import java.io.IOException;

import chiel.jara.stipjestrip.model.Comic;
import chiel.jara.stipjestrip.model.ComicDatabase;
import chiel.jara.stipjestrip.model.ComicDatasource;
import chiel.jara.stipjestrip.util.ComicAdapter;
import chiel.jara.stipjestrip.util.ComicHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListActivity extends AppCompatActivity {

    private RecyclerView rvComics;
    private ComicHandler myComicHandler;
    private ComicAdapter myComicAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiclist);
        rvComics=findViewById(R.id.rv_comics);

        //recyclerview instellen:
        ComicAdapter myComicAdapter = new ComicAdapter(ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllComics());
        rvComics.setAdapter(myComicAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComics.setLayoutManager(linearLayoutManager);

        myComicHandler=new ComicHandler(myComicAdapter, getApplicationContext());
        downloadData();
        myComicAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void downloadData(){
        Thread backThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://bruxellesdata.opendatasoft.com/api/records/1.0/search/?dataset=comic-book-route&rows=50").get().build();
                    Response response = client.newCall(request).execute();
                    //Response roImg = client.newCall(rImg).execute();

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
        myComicAdapter.notifyDataSetChanged();
        //TODO als data is binnengehaald, dan wordt launchscreen gesloten en wordt map_activity getoond
    }
}
