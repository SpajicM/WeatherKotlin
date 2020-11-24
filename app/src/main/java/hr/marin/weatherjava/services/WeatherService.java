package hr.marin.weatherjava.services;

import java.util.List;

import androidx.annotation.NonNull;
import hr.marin.weatherjava.API;
import hr.marin.weatherjava.models.Location;
import hr.marin.weatherjava.models.SearchResult;
import hr.marin.weatherjava.services.interfaces.IWeatherService;
import hr.marin.weatherjava.services.listeners.IWeatherServiceListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService {
    private IWeatherService service = API.getRetrofit().create(IWeatherService.class);

    public void findLocationByName(String name, final IWeatherServiceListener<List<SearchResult>> handleWeatherService) {
        Call<List<SearchResult>> call = service.findLocationByName(name);
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchResult>> call, @NonNull Response<List<SearchResult>> response) {
                List<SearchResult> searchResults = response.body();
                handleWeatherService.onSuccess(searchResults);
            }
            @Override
            public void onFailure(@NonNull Call<List<SearchResult>> call, @NonNull Throwable t) {
                handleWeatherService.onFailure(t);
            }
        });
    }

    public void findLocationByCoordinates(String latitude, String longitude, final IWeatherServiceListener<List<SearchResult>> handleWeatherService) {
        String joinedCoordinates = latitude + "," + longitude;
        Call<List<SearchResult>> call = service.findLocationByCoordinates(joinedCoordinates);
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<SearchResult>> call, @NonNull Response<List<SearchResult>> response) {
                List<SearchResult> searchResults = response.body();
                handleWeatherService.onSuccess(searchResults);
            }
            @Override
            public void onFailure(@NonNull Call<List<SearchResult>> call, @NonNull Throwable t) {
                handleWeatherService.onFailure(t);
            }
        });
    }

    public void getLocationWeather(String id, final IWeatherServiceListener<Location> handleWeatherService) {
        Call<Location> call = service.getLocationWeather(id);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(@NonNull Call<Location> call, @NonNull Response<Location> response) {
                Location locationWeather = response.body();
                handleWeatherService.onSuccess(locationWeather);
            }
            @Override
            public void onFailure(@NonNull Call<Location> call, @NonNull Throwable t) {
                handleWeatherService.onFailure(t);
            }
        });
    }
}
