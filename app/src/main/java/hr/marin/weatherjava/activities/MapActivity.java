package hr.marin.weatherjava.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import hr.marin.weatherjava.R;
import hr.marin.weatherjava.presenters.MapPresenter;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapPresenter.View {
    public static final String INTENT_CITY_ID = "CITY_ID";

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;

    private MapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        presenter = new MapPresenter(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(latLng -> {
            presenter.getCoordinatesCity(latLng.latitude, latLng.longitude);
        });

        presenter.requestPermissions();
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public Activity getViewActivity() {
        return this;
    }

    @SuppressLint("MissingPermission") // checked in presenter
    @Override
    public void permissionGranted() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mGoogleMap.setMyLocationEnabled(true);
    }

    @Override
    public void permissionDenied() {
        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showWeather(int id) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra(INTENT_CITY_ID, id);
        startActivity(intent);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Location not found", Toast.LENGTH_LONG).show();
    }
}
