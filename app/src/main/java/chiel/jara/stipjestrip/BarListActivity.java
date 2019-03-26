package chiel.jara.stipjestrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import chiel.jara.stipjestrip.model.bar_model.BarDatabase;
import chiel.jara.stipjestrip.util.bar_util.BarAdapter;

public class BarListActivity extends AppCompatActivity {

    private RecyclerView rvBars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_activity_list);
        rvBars = findViewById(R.id.rv_bars);

        BarAdapter myBarAdapter = new BarAdapter(BarDatabase.getInstance(getApplicationContext()).getMethodsBar().getAllBars());
        rvBars.setAdapter(myBarAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvBars.setLayoutManager(linearLayoutManager);
    }
}
