package chiel.jara.stipjestrip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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

    //Aanmaken Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView();
        return super.onCreateOptionsMenu(menu);
    }


}
