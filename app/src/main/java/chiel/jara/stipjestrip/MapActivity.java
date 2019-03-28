package chiel.jara.stipjestrip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.io.IOException;
import java.util.List;
import java.util.Map;

import chiel.jara.stipjestrip.model.bar_model.Bar;
import chiel.jara.stipjestrip.model.bar_model.BarDatabase;
import chiel.jara.stipjestrip.model.comic_model.Comic;
import chiel.jara.stipjestrip.model.comic_model.ComicDatabase;
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
                Intent intentC = new Intent(getApplicationContext(), ComicListActivity.class);
                intentC.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentC);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_café:
                Intent intentB = new Intent(getApplicationContext(), BarListActivity.class);
                intentB.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentB);
                drawerLayout.closeDrawers();
                break;
            case R.id.btn_about:
                Intent intentA = new Intent(getApplicationContext(), AboutActivity.class);
                intentA.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentA);
                drawerLayout.closeDrawers();
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
                    //FOR BAR
                if (marker.getTag() instanceof Bar) {
                    View myContentBarView = getLayoutInflater().inflate(R.layout.info_window_map_bar, null, false);
                    TextView tvBarName = myContentBarView.findViewById(R.id.tv_marker_bar_name);
                    tvBarName.setText(marker.getTitle());
                    TextView tvBarAddress = myContentBarView.findViewById(R.id.tv_marker_bar_address);
                    tvBarAddress.setText(marker.getSnippet());

                    return myContentBarView;
                } else {
                    //FOR COMIC
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
        //add comic markers
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
        //add bar markers
        for (Bar bar : BarDatabase.getInstance(getApplicationContext()).getMethodsBar().getAllBars()){

            Geocoder geocoder = new Geocoder(getApplicationContext());
            String barAddress = bar.getStreet() +" "+ bar.getHouseNumber() + ", " + bar.getPostalcode() +" "+ bar.getCity();
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(barAddress, 1);
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                LatLng barLatLng = new LatLng(latitude, longitude);
                float barKleur = 100;
                Marker barMarker = map.addMarker(
                        new MarkerOptions()
                                .title(bar.getName())
                                .snippet(barAddress)
                                .position(barLatLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(barKleur))
                );
                barMarker.setTag(bar);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
         if (marker.getTag() instanceof Comic) {
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("comic", (Comic) marker.getTag());
                startActivity(detailIntent);
            }
            if (marker.getTag() instanceof Bar) {
                Intent detailIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailIntent.putExtra("bar", (Bar) marker.getTag());
                startActivity(detailIntent);
            }
    }
}
//DOCUMENTATION: how to zoom on current location : https://stackoverflow.com/questions/18425141/android-google-maps-api-v2-zoom-to-current-location
//DOCUMENTATION: how to click on marker : https://stackoverflow.com/questions/14226453/google-maps-api-v2-how-to-make-markers-clickable   https://blog.fossasia.org/marker-click-management-in-android-google-map-api-version-2/   https://stackoverflow.com/questions/39446198/how-to-use-onmarkerclick-to-open-a-new-activity-for-google-map-android-api
//DOCUMENTATION: how to change infowindow of marker : https://stackoverflow.com/questions/21678545/how-to-change-info-window-custom-position-in-google-map-v2-android   https://mobikul.com/android-setting-custom-info-window-google-map-marker/   https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.InfoWindowAdapter
//DOCUMENTATION: how to get address from coordinates : https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude   https://developer.android.com/reference/android/location/Geocoder   https://stackoverflow.com/questions/472313/android-reverse-geocoding-getfromlocation
//DOCUMENTATION: how to get coordinates from address : https://stackoverflow.com/questions/9698328/how-to-get-coordinates-of-an-address-in-android
