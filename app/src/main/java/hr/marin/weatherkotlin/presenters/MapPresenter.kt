package hr.marin.weatherkotlin.presenters

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hr.marin.weatherkotlin.R
import hr.marin.weatherkotlin.models.SearchResult
import hr.marin.weatherkotlin.services.WeatherService
import hr.marin.weatherkotlin.services.listeners.IWeatherServiceListener


class MapPresenter(val view: View) {
    private val service: WeatherService = WeatherService()

    fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(view.getViewActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                view.permissionGranted()
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            view.permissionGranted()
        }
    }
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(view.getViewActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(view.getViewActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(view.getViewActivity())
                    .setTitle(R.string.permission_needed)
                    .setMessage(R.string.permission_needed_description)
                    .setPositiveButton("OK") { dialog, which ->

                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            view.getViewActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            PERMISSIONS_REQUEST_LOCATION
                        )
                    }.create().show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(view.getViewActivity(),
                     arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_LOCATION )
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
         grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(view.getViewActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                    view.permissionGranted()
                }

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                view.permissionDenied()
            }
        }
    }

    fun getCoordinatesCity(latitude: Double, longitude: Double) {
        service.findLocationByCoordinates(latitude, longitude,  object: IWeatherServiceListener<List<SearchResult>> {

            override fun onSuccess(response: List<SearchResult>) {
                val city: SearchResult = response[0]
                view.showWeather(city.woeid)
            }

            override fun onFailure(t: Throwable) {
                view.onError()
            }
        })
    }

    companion object {
        const val PERMISSIONS_REQUEST_LOCATION = 1
    }

    interface View {
        fun getViewActivity(): Activity
        fun permissionGranted()
        fun permissionDenied()
        fun showWeather(id: Int)
        fun onError()
    }
}