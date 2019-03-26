package chiel.jara.stipjestrip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import chiel.jara.stipjestrip.model.Comic;
import chiel.jara.stipjestrip.model.ComicDatabase;

/**
 * Created By Chiel&Jara 03/2019
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener ,NavigationView.OnNavigationItemSelectedListener{


    private final int REQUEST_LOCATION = 1; //constante variabele voor bij permissions
    private GoogleMap map;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBar actionbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment_layout, supportMapFragment).commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    //Aanmaken Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    //Iets selecteren van uit het menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.sw_strip:
                break;
            case R.id.sw_café:
                break;
            case R.id.btn_strips:
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_café:
                drawerLayout.closeDrawers();
                break;
        }
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        setUpCamera();
        addMarkers();
        map.setOnInfoWindowClickListener(this);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View myContentView = getLayoutInflater().inflate(R.layout.info_window_map, null, false);
                TextView textView = myContentView.findViewById(R.id.tv_info_title);
                textView.setText(marker.getTitle());
                ImageView imageView = myContentView.findViewById(R.id.iv_info_image);
                try {
                    FileInputStream fis = getApplicationContext().openFileInput(marker.getSnippet());
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return myContentView;
            }
        });
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_LOCATION);
            } else {
                map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);
        }
    }

    //SHOW MAP ;
    private void setUpCamera() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (myLocation != null){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 15));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())).zoom(15).tilt(40).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                map.animateCamera(cameraUpdate);
            }
        }else{
            CameraPosition.Builder backUpBuilder = new CameraPosition.Builder();
            CameraPosition backUpPosition = backUpBuilder.target(new LatLng(50.848712,4.347446)).zoom(18).tilt(60).build();
            CameraUpdate updateBackUp = CameraUpdateFactory.newCameraPosition(backUpPosition);
            map.animateCamera(updateBackUp);
        }


    }

    private void addMarkers() {
        for (Comic comic : ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllComics()) {
            LatLng latLng = new LatLng(comic.getCoordinateLAT(), comic.getCoordinateLONG());
            float kleur = 195;
            Marker newMarker = map.addMarker(
                    new MarkerOptions()
                            .title(comic.getName() +" - "+ comic.getAuthor())
                            .snippet(comic.getImgID())
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(kleur))
            );
            newMarker.setTag(comic);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION){
            for (int result : grantResults)
                if (result == PackageManager.PERMISSION_GRANTED)
                    map.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
        detailIntent.putExtra("comic", (Comic)marker.getTag());
        startActivity(detailIntent);
    }
}
