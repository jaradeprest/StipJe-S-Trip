package chiel.jara.stipjestrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    ImageView ivGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivGif = findViewById(R.id.iv_walkingman);
        Glide.with(getApplicationContext()).load(R.drawable.marsupilami).into(ivGif);  //imported glide dependencies in build.gradle
    }
}
