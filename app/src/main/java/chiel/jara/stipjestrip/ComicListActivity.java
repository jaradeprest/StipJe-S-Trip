package chiel.jara.stipjestrip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.SearchView;

import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;
import chiel.jara.stipjestrip.util.comic_util.ComicAdapter;

/**
 * Created By Chiel&Jara 03/2019
 */

public class ComicListActivity extends AppCompatActivity {

    private RecyclerView rvComics;
    private ComicAdapter myComicAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiclist);
        rvComics = findViewById(R.id.rv_comics);

        //recyclerview instellen:
        myComicAdapter = new ComicAdapter(ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllComics());
        rvComics.setAdapter(myComicAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComics.setLayoutManager(linearLayoutManager);
    }

    //TODO click on listitem en get send to map => right pin




    //Aanmaken Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView();
        searchView.setOnQueryTextListener(textListener);

        return super.onCreateOptionsMenu(menu);
    }

    public SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            myComicAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            myComicAdapter.getFilter().filter(newText);
            return false;
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent reloadIntent = new Intent(this, ComicListActivity.class);
        reloadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(reloadIntent);
    }
}
