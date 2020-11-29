package hr.marin.weatherkotlin.activities
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import hr.marin.weatherkotlin.R
import hr.marin.weatherkotlin.presenters.MapPresenter

class MapActivity: AppCompatActivity(), OnMapReadyCallback, MapPresenter.View {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapFrag: SupportMapFragment
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLastLocation: Location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var presenter: MapPresenter = MapPresenter(this)

    private var mLocationCallback:LocationCallback = object:LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0)
            {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                mLastLocation = location
                val latLng = LatLng(location.latitude, location.longitude)
                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11F))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)
    }
    override fun onPause() {
        super.onPause()
        //stop location updates when Activity is no longer active
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onMapReady(googleMap:GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 60000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        // Setting a click event handler for the map
        googleMap.setOnMapClickListener { latLng-> presenter.getCoordinatesCity(latLng.latitude, latLng.longitude) }
        presenter.requestPermissions()
    }
    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults:IntArray) {
        presenter.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun getViewActivity(): Activity {
        return this
    }

    @SuppressLint("MissingPermission") // checked in presenter
    override fun permissionGranted() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        mGoogleMap.isMyLocationEnabled = true
    }
    override fun permissionDenied() {
        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
    }

    override fun showWeather(id:Int) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra(WeatherActivity.INTENT_CITY_ID, id)
        startActivity(intent)
    }

    override fun onError() {
        Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_LONG).show()
    }
}