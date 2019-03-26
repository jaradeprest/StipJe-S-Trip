package chiel.jara.stipjestrip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;
import chiel.jara.stipjestrip.util.comic_util.ComicAdapter;

/**
 * Created By Chiel&Jara 03/2019
 */

public class ListActivity extends AppCompatActivity {

    private RecyclerView rvComics;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiclist);
        rvComics = findViewById(R.id.rv_comics);

        //recyclerview instellen:
        ComicAdapter myComicAdapter = new ComicAdapter(ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllComics());
        rvComics.setAdapter(myComicAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComics.setLayoutManager(linearLayoutManager);
    }


}
