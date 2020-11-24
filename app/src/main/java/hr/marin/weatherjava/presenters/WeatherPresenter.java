package hr.marin.weatherjava.presenters;

import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public String formatDate(String oldString, String newFormat) {
        final String OLD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.US);
        Date d = null;
        try {
            d = sdf.parse(oldString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(newFormat);
        return sdf.format(d);
    }


    public String formatWeekday(String dateString) {
            final String OLD_FORMAT = "yyyy-MM-dd";

            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.US);
            Date d = null;
            try {
                d = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            sdf.applyPattern("EEEE");
            return sdf.format(d);
    }

    public void getLocationWeather(String id) {
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
