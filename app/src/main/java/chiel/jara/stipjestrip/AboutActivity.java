package chiel.jara.stipjestrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created By Chiel&Jara 03/2019
 */
public class AboutActivity extends AppCompatActivity {

    ImageView background;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        background=findViewById(R.id.iv_background_about);
        Glide.with(this).load(R.drawable.about).into(background);
    }
}
