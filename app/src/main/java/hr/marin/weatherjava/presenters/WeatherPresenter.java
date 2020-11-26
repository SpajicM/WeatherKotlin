package hr.marin.weatherjava.presenters;

import android.view.View;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hr.marin.weatherjava.models.ConsolidatedWeather;
import hr.marin.weatherjava.models.Location;
import hr.marin.weatherjava.services.WeatherService;
import hr.marin.weatherjava.services.listeners.IWeatherServiceListener;

public class WeatherPresenter {
    private View view;
    private WeatherService service;

    public WeatherPresenter(final View view) {
        this.view = view;
        this.service = new WeatherService();
    }

    public void getLocationWeather(int id) {
        view.showLoader();
        service.getLocationWeather(id, new IWeatherServiceListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                view.onFetchSuccess(location);
                view.hideLoader();
            }

            @Override
            public void onFailure(Throwable t) {
                view.onError();
                view.hideLoader();
            }
        });
    }

    public interface View {
        void showLoader();
        void hideLoader();
        void onFetchSuccess(Location location);
        void onError();
    }
}
