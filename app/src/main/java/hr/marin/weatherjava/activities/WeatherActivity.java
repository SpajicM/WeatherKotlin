package hr.marin.weatherjava.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import hr.marin.weatherjava.R;
import hr.marin.weatherjava.models.ConsolidatedWeather;
import hr.marin.weatherjava.models.Location;
import hr.marin.weatherjava.presenters.WeatherPresenter;

public class WeatherActivity extends AppCompatActivity implements WeatherPresenter.View{

    private WeatherPresenter presenter;

    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, predictabilityTxt;

    private TextView nameDay1, nameDay2, nameDay3, nameDay4, stateDay1, stateDay2, stateDay3, stateDay4;

    private View loader, mainContainer, errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);

        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        predictabilityTxt = findViewById(R.id.predictability);
        nameDay1 = findViewById(R.id.name_day1);
        nameDay2 = findViewById(R.id.name_day2);
        nameDay3 = findViewById(R.id.name_day3);
        nameDay4 = findViewById(R.id.name_day4);
        stateDay1 = findViewById(R.id.state_day1);
        stateDay2 = findViewById(R.id.state_day2);
        stateDay3 = findViewById(R.id.state_day3);
        stateDay4 = findViewById(R.id.state_day4);
        errorText = findViewById(R.id.errorText);
        loader = findViewById(R.id.loader);
        mainContainer = findViewById(R.id.mainContainer);

        presenter = new WeatherPresenter(this);
        presenter.getLocationWeather("44418");

    }

    @Override
    public void showLoader() {
        loader.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }

    @Override
    public void hideLoader() {
        loader.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchSuccess(Location location) {
        ConsolidatedWeather today = location.getConsolidatedWeather().get(0);

        addressTxt.setText(location.getTitle());
        updated_atTxt.setText(String.format("at %s", presenter.formatDate(today.getCreated(), "HH:mm")));
        statusTxt.setText(today.getWeatherStateName());
        tempTxt.setText(String.format(Locale.getDefault(),"%d°C", today.getTheTemp().intValue()));
        temp_minTxt.setText(String.format(Locale.getDefault(),"Min: %d°C", today.getMinTemp().intValue()));
        temp_maxTxt.setText(String.format(Locale.getDefault(),"Max: %d°C", today.getMaxTemp().intValue()));
        sunriseTxt.setText(presenter.formatDate(location.getSunRise(), "HH:mm"));
        sunsetTxt.setText(presenter.formatDate(location.getSunSet(), "HH:mm"));
        windTxt.setText(String.format(Locale.getDefault(), "%.3f", today.getWindSpeed()));
        pressureTxt.setText(today.getAirPressure().toString());
        humidityTxt.setText(String.format(Locale.getDefault(),"%d%%", today.getHumidity()));
        predictabilityTxt.setText(String.format(Locale.getDefault(),"%d%%", today.getPredictability()));

        ConsolidatedWeather day1 = location.getConsolidatedWeather().get(1);
        ConsolidatedWeather day2 = location.getConsolidatedWeather().get(2);
        ConsolidatedWeather day3 = location.getConsolidatedWeather().get(3);
        ConsolidatedWeather day4 = location.getConsolidatedWeather().get(4);

        nameDay1.setText(presenter.formatWeekday(day1.getApplicableDate()));
        nameDay2.setText(presenter.formatWeekday(day2.getApplicableDate()));
        nameDay3.setText(presenter.formatWeekday(day3.getApplicableDate()));
        nameDay4.setText(presenter.formatWeekday(day4.getApplicableDate()));

        stateDay1.setText(day1.getWeatherStateName());
        stateDay2.setText(day2.getWeatherStateName());
        stateDay3.setText(day3.getWeatherStateName());
        stateDay4.setText(day4.getWeatherStateName());
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.search_failed, Toast.LENGTH_LONG).show();
        loader.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
    }
}