package hr.marin.weatherkotlin.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.marin.weatherkotlin.R
import hr.marin.weatherkotlin.adapters.WeatherItemAdapter
import hr.marin.weatherkotlin.models.Location
import hr.marin.weatherkotlin.presenters.WeatherPresenter
import hr.marin.weatherkotlin.utils.DateParser
import java.util.*

class WeatherActivity: AppCompatActivity(), WeatherPresenter.View {
    private val presenter = WeatherPresenter(this)
    private lateinit var addressTxt: TextView
    private lateinit var updatedAtTxt: TextView
    private lateinit var statusTxt: TextView
    private lateinit var tempTxt: TextView
    private lateinit var tempMinTxt: TextView
    private lateinit var tempMaxTxt: TextView
    private lateinit var sunriseTxt: TextView
    private lateinit var sunsetTxt: TextView
    private lateinit var windTxt: TextView
    private lateinit var pressureTxt: TextView
    private lateinit var humidityTxt: TextView
    private lateinit var predictabilityTxt: TextView
    private lateinit var rvWeatherState: RecyclerView
    private lateinit var loader: View
    private lateinit var mainContainer: View
    private lateinit var errorText: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        addressTxt = findViewById(R.id.address)
        updatedAtTxt = findViewById(R.id.updated_at)
        statusTxt = findViewById(R.id.status)
        tempTxt = findViewById(R.id.temp)
        tempMinTxt = findViewById(R.id.temp_min)
        tempMaxTxt = findViewById(R.id.temp_max)
        sunriseTxt = findViewById(R.id.sunrise)
        sunsetTxt = findViewById(R.id.sunset)
        windTxt = findViewById(R.id.wind)
        pressureTxt = findViewById(R.id.pressure)
        humidityTxt = findViewById(R.id.humidity)
        predictabilityTxt = findViewById(R.id.predictability)
        errorText = findViewById(R.id.errorText)
        loader = findViewById(R.id.loader)
        mainContainer = findViewById(R.id.mainContainer)
        rvWeatherState = findViewById(R.id.state_rv)

        val intent = intent
        val cityId = intent.getIntExtra(INTENT_CITY_ID, 0)
        if (cityId != 0) {
            presenter.getLocationWeather(cityId)
        } else {
            onError()
        }
    }

    override fun showLoader() {
        loader.visibility = View.VISIBLE
        mainContainer.visibility = View.GONE
        errorText.visibility = View.GONE
    }

    override fun hideLoader() {
        loader.visibility = View.GONE
        mainContainer.visibility = View.VISIBLE
    }

    override fun onFetchSuccess(location: Location) {
        val today = location.consolidated_weather[0]
        addressTxt.text = location.title
        updatedAtTxt.text = String.format("at %s", DateParser.formatDate(today.created, "HH:mm"))
        statusTxt.text = today.weather_state_name
        tempTxt.text = String.format(Locale.getDefault(), "%.0f°C", today.the_temp)
        tempMinTxt.text = String.format(Locale.getDefault(), "Min: %.0f°C", today.min_temp)
        tempMaxTxt.text = String.format(Locale.getDefault(), "Max: %.0f°C", today.max_temp)
        sunriseTxt.text = DateParser.formatDate(location.sun_rise, "HH:mm")
        sunsetTxt.text = DateParser.formatDate(location.sun_set, "HH:mm")
        windTxt.text = String.format(Locale.getDefault(), "%.3f", today.wind_speed)
        pressureTxt.text = String.format(Locale.getDefault(), "%.1f Pa", today.air_pressure)
        humidityTxt.text = String.format(Locale.getDefault(), "%d%%", today.humidity)
        predictabilityTxt.text = String.format(Locale.getDefault(), "%d%%", today.predictability)

        val list = location.consolidated_weather.subList(1, location.consolidated_weather.size)
        val adapter = WeatherItemAdapter(list)
        rvWeatherState.adapter = adapter
        rvWeatherState.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onError() {
        Toast.makeText(this, R.string.weather_failed, Toast.LENGTH_LONG).show()
        mainContainer.visibility = View.GONE
        loader.visibility = View.GONE
        errorText.visibility = View.VISIBLE
    }

    companion object {
        const val INTENT_CITY_ID = "CITY_ID"
    }
}