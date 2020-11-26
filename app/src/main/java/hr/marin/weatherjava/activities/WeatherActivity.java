package hr.marin.weatherjava.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hr.marin.weatherjava.R;
import hr.marin.weatherjava.adapters.WeatherItemAdapter;
import hr.marin.weatherjava.models.ConsolidatedWeather;
import hr.marin.weatherjava.models.Location;
import hr.marin.weatherjava.presenters.MapPresenter;
import hr.marin.weatherjava.presenters.WeatherPresenter;
import hr.marin.weatherjava.utils.DateParser;

public class WeatherActivity extends AppCompatActivity implements WeatherPresenter.View{
    public static final String INTENT_CITY_ID = "CITY_ID";

    private WeatherPresenter presenter;

    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, predictabilityTxt;

    private RecyclerView rvWeatherState;

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
        errorText = findViewById(R.id.errorText);
        loader = findViewById(R.id.loader);
        mainContainer = findViewById(R.id.mainContainer);
        rvWeatherState = (RecyclerView) findViewById(R.id.state_rv);

        presenter = new WeatherPresenter(this);

        Intent intent = getIntent();
        int cityId = intent.getIntExtra(INTENT_CITY_ID, 0);

        if(cityId != 0) {
            presenter.getLocationWeather(cityId);
        } else {
            onError();
        }
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
        updated_atTxt.setText(String.format("at %s", DateParser.formatDate(today.getCreated(), "HH:mm")));
        statusTxt.setText(today.getWeatherStateName());
        tempTxt.setText(String.format(Locale.getDefault(),"%d°C", today.getTheTemp().intValue()));
        temp_minTxt.setText(String.format(Locale.getDefault(),"Min: %d°C", today.getMinTemp().intValue()));
        temp_maxTxt.setText(String.format(Locale.getDefault(),"Max: %d°C", today.getMaxTemp().intValue()));
        sunriseTxt.setText(DateParser.formatDate(location.getSunRise(), "HH:mm"));
        sunsetTxt.setText(DateParser.formatDate(location.getSunSet(), "HH:mm"));
        windTxt.setText(String.format(Locale.getDefault(), "%.3f", today.getWindSpeed()));
        pressureTxt.setText(String.format(Locale.getDefault(), "%.1f Pa", today.getAirPressure()));
        humidityTxt.setText(String.format(Locale.getDefault(),"%d%%", today.getHumidity()));
        predictabilityTxt.setText(String.format(Locale.getDefault(),"%d%%", today.getPredictability()));

        List<ConsolidatedWeather> list = location.getConsolidatedWeather().subList(1, location.getConsolidatedWeather().size());
        WeatherItemAdapter adapter = new WeatherItemAdapter(list);
        rvWeatherState.setAdapter(adapter);
        rvWeatherState.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onError() {
        Toast.makeText(this, R.string.weather_failed, Toast.LENGTH_LONG).show();
        mainContainer.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        errorText.setVisibility(View.VISIBLE);
    }
}