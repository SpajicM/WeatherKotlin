package hr.marin.weatherjava.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import hr.marin.weatherjava.R;
import hr.marin.weatherjava.activities.MapActivity;
import hr.marin.weatherjava.models.Location;
import hr.marin.weatherjava.models.SearchResult;
import hr.marin.weatherjava.services.WeatherService;
import hr.marin.weatherjava.services.listeners.IWeatherServiceListener;

public class MapPresenter {

    public static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private View view;
    private WeatherService service;

    public MapPresenter(final View view) {
        this.view = view;
        this.service = new WeatherService();
    }

    public void requestPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(view.getViewActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                view.permissionGranted();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            view.permissionGranted();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(view.getViewActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(view.getViewActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(view.getViewActivity())
                        .setTitle(R.string.permission_needed)
                        .setMessage(R.string.permission_needed_description)
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(view.getViewActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSIONS_REQUEST_LOCATION );
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(view.getViewActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(view.getViewActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    view.permissionGranted();
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                view.permissionDenied();
            }
        }
    }

    public void getCoordinatesCity(double latitude, double longitude) {
        service.findLocationByCoordinates(latitude, longitude, new IWeatherServiceListener<List<SearchResult>>() {
            @Override
            public void onSuccess(List<SearchResult> response) {
                SearchResult city = response.get(0);
                if(city != null) {
                    view.showWeather(city.getWoeid());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                view.onError();
            }
        });
    }


    public interface View {
        public Activity getViewActivity();
        public void permissionGranted();
        public void permissionDenied();
        public void showWeather(int id);
        public void onError();
    }
}
