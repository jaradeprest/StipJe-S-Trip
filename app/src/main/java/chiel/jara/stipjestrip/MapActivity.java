package chiel.jara.stipjestrip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
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
import java.util.ArrayList;
import java.util.List;

import chiel.jara.stipjestrip.model.bar_model.Bar;
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
    private MenuItem miStrips, miBars;
    private Switch swiStrip, swiBars;
    private Context context;
    ArrayList<Marker> comicMarkers;
    ArrayList<Marker> barMarkers;

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

        miStrips = navigationView.getMenu().findItem(R.id.sw_strip);
        swiStrip = miStrips.getActionView().findViewById(R.id.swi_for_menu);
        miBars = navigationView.getMenu().findItem(R.id.sw_café);
        swiBars = miBars.getActionView().findViewById(R.id.swi_for_menu);

        swiStrip.setOnCheckedChangeListener(changeListener);
        swiBars.setOnCheckedChangeListener(changeListener);
        context = this;

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
        List<Bar> bars = ComicDatabase.getInstance(context).getMethodsComic().getAllBars();
        @Override
        public boolean onQueryTextSubmit(String query) {
            for (Marker comicMarker : comicMarkers) {
                Comic comic = (Comic) comicMarker.getTag();
                if (comic.getName().equalsIgnoreCase(query)) {
                    CameraPosition.Builder backUpBuilder = new CameraPosition.Builder();
                    CameraPosition backUpPosition = backUpBuilder.target(new LatLng(comic.getCoordinateLAT(),comic.getCoordinateLONG())).zoom(18).tilt(60).build();
                    CameraUpdate updateBackUp = CameraUpdateFactory.newCameraPosition(backUpPosition);
                    map.animateCamera(updateBackUp);
                   comicMarker.showInfoWindow();
                    return true;
                }
            }
            for (Marker barMarker : barMarkers) {
                Bar bar = (Bar) barMarker.getTag();
                if (bar.getName().equalsIgnoreCase(query)) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    String barAddress = bar.getStreet() +" "+ bar.getHouseNumber() + ", " + bar.getPostalcode() +" "+ bar.getCity();
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocationName(barAddress, 1);

                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();

                        CameraPosition.Builder backUpBuilder = new CameraPosition.Builder();
                        CameraPosition backUpPosition = backUpBuilder.target(new LatLng(latitude,longitude)).zoom(18).tilt(60).build();
                        CameraUpdate updateBackUp = CameraUpdateFactory.newCameraPosition(backUpPosition);
                        map.animateCamera(updateBackUp);
                        barMarker.showInfoWindow();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

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

    public CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            map.clear();
            if (swiStrip.isChecked()) {
                addComicMarkers(); }
            if (swiBars.isChecked()) {
                addBarMarkers(); }
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.sw_strip:
                if (swiStrip.isChecked()) {
                    swiStrip.setChecked(false);
                } else {
                    swiStrip.setChecked(true);
                }
                swiStrip.setOnCheckedChangeListener(changeListener);
                break;
            case R.id.sw_café:
                if (swiBars.isChecked()) {
                    swiBars.setChecked(false);
                } else {
                    swiBars.setChecked(true);
                }
                swiBars.setOnCheckedChangeListener(changeListener);
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
                    TextView tvRating = myContentBarView.findViewById(R.id.tv_rating);
                    if (((Bar) marker.getTag()).isRated()){
                    String rating = String.valueOf(((Bar) marker.getTag()).getRating());
                    tvRating.setText("Rating: "+ rating + " / 10");
                    }else{tvRating.setVisibility(View.INVISIBLE);}
                    return myContentBarView;
                } else {
                    //FOR COMIC
                    View myContentView = getLayoutInflater().inflate(R.layout.info_window_map, null, false);
                    TextView textView = myContentView.findViewById(R.id.tv_info_title);
                    textView.setText(marker.getTitle());

                    //show heart when comic is favorite
                    ImageButton imageButton = myContentView.findViewById(R.id.ib_favo);
                    ImageButton visitedButton = myContentView.findViewById(R.id.ib_visited);
                    if (((Comic) marker.getTag()).isFavorite()){
                        imageButton.setColorFilter(Color.RED);
                    }else{imageButton.setVisibility(View.INVISIBLE);
                    }

                    if (((Comic)marker.getTag()).isVisited()){
                        visitedButton.setColorFilter(Color.WHITE);
                    }else {
                        if (!((Comic)marker.getTag()).isVisited()){
                        visitedButton.setVisibility(View.INVISIBLE);
                        }else if (((Comic)marker.getTag()).isVisited() && !((Comic)marker.getTag()).isFavorite()){
                            imageButton.setImageResource(R.drawable.seen_icon);
                            imageButton.setColorFilter(Color.WHITE);
                        }else {visitedButton.setVisibility(View.INVISIBLE);}
                    }

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
            }
        }else{
            CameraPosition.Builder backUpBuilder = new CameraPosition.Builder();
            CameraPosition backUpPosition = backUpBuilder.target(new LatLng(50.848712,4.347446)).zoom(18).tilt(60).build();
            CameraUpdate updateBackUp = CameraUpdateFactory.newCameraPosition(backUpPosition);
            map.animateCamera(updateBackUp);
        }


    }

    private void addMarkers() {
        addComicMarkers();
        addBarMarkers();
    }

    private void addBarMarkers() {
        //add bar markers
        barMarkers = new ArrayList<>();
        for (Bar bar : ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllBars()){

            Geocoder geocoder = new Geocoder(getApplicationContext());
            String barAddress = bar.getStreet() +" "+ bar.getHouseNumber() + ", " + bar.getPostalcode() +" "+ bar.getCity();
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(barAddress, 1);
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                LatLng barLatLng = new LatLng(latitude, longitude);

                Marker barMarker = map.addMarker(
                        new MarkerOptions()
                                .title(bar.getName())
                                .snippet(barAddress)
                                .position(barLatLng)
                );

                if (bar.isRated()){
                    int height = 120; int width = 100;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_rated);
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    Bitmap favoMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    barMarker.setIcon(BitmapDescriptorFactory.fromBitmap(favoMarker));
                }else{
                    int height = 120; int width = 85;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_bar);
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    Bitmap comicMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    barMarker.setIcon(BitmapDescriptorFactory.fromBitmap(comicMarker));
                }

                barMarker.setTag(bar);
                barMarkers.add(barMarker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addComicMarkers() {
        //add comic markers
        comicMarkers = new ArrayList<>();
        for (Comic comic : ComicDatabase.getInstance(getApplicationContext()).getMethodsComic().getAllComics()) {
            LatLng latLng = new LatLng(comic.getCoordinateLAT(), comic.getCoordinateLONG());

            Marker newMarker = map.addMarker(
                    new MarkerOptions()
                            .title(comic.getName() +" - "+ comic.getAuthor())
                            .snippet(comic.getImgID())
                            .position(latLng)
            );

            if (comic.isFavorite()){
                int height = 120; int width = 100;
                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_favo);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                Bitmap favoMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
                newMarker.setIcon(BitmapDescriptorFactory.fromBitmap(favoMarker));
            }else{
                if (comic.isVisited()){
                    int height = 120; int width = 100;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_comic_seen);
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    Bitmap visitedMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    newMarker.setIcon(BitmapDescriptorFactory.fromBitmap(visitedMarker));
                }else {
                    int height = 120;
                    int width = 85;
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_comic);
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    Bitmap comicMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    newMarker.setIcon(BitmapDescriptorFactory.fromBitmap(comicMarker));
                }
            }

            newMarker.setTag(comic);
            comicMarkers.add(newMarker);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        map.clear();
        if (swiBars.isChecked()){
            addBarMarkers();
        }
        if (swiStrip.isChecked()){
            addComicMarkers();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
//DOCUMENTATION: how to zoom on current location : https://stackoverflow.com/questions/18425141/android-google-maps-api-v2-zoom-to-current-location
//DOCUMENTATION: how to click on marker : https://stackoverflow.com/questions/14226453/google-maps-api-v2-how-to-make-markers-clickable   https://blog.fossasia.org/marker-click-management-in-android-google-map-api-version-2/   https://stackoverflow.com/questions/39446198/how-to-use-onmarkerclick-to-open-a-new-activity-for-google-map-android-api
//DOCUMENTATION: how to change infowindow of marker : https://stackoverflow.com/questions/21678545/how-to-change-info-window-custom-position-in-google-map-v2-android   https://mobikul.com/android-setting-custom-info-window-google-map-marker/   https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.InfoWindowAdapter
//DOCUMENTATION: how to get address from coordinates : https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude   https://developer.android.com/reference/android/location/Geocoder   https://stackoverflow.com/questions/472313/android-reverse-geocoding-getfromlocation
//DOCUMENTATION: how to get coordinates from address : https://stackoverflow.com/questions/9698328/how-to-get-coordinates-of-an-address-in-android
