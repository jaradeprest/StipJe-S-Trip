package chiel.jara.stipjestrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.SearchView;

import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;
import chiel.jara.stipjestrip.util.bar_util.BarAdapter;
/**
 * Created By Chiel&Jara 03/2019
 */
public class BarListActivity extends AppCompatActivity {

    private RecyclerView rvBars;
    private BarAdapter myBarAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_activity_list);
        rvBars = findViewById(R.id.rv_bars);

        myBarAdapter = new BarAdapter(ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllBars());
        rvBars.setAdapter(myBarAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvBars.setLayoutManager(linearLayoutManager);
    }

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
            myBarAdapter.getFilter().filter(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            myBarAdapter.getFilter().filter(newText);
            return false;
        }
    };
}
