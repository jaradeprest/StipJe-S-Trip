package chiel.jara.stipjestrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import chiel.jara.stipjestrip.model.comic_model.Comic;
import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;
import chiel.jara.stipjestrip.util.comic_util.ComicAdapter;

public class MyFavComicActivity extends AppCompatActivity {

    private RecyclerView rvComics;
    private ComicAdapter myComicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiclist);

        rvComics = findViewById(R.id.rv_comics);

        //recyclerview instellen:
        List<Comic> myFavComicList = ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getFavComics();
        myComicAdapter = new ComicAdapter(myFavComicList);
        rvComics.setAdapter(myComicAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComics.setLayoutManager(linearLayoutManager);

    }
}
