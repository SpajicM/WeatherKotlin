package hr.marin.weatherjava.services.interfaces;

import java.util.List;

import hr.marin.weatherjava.models.Location;
import hr.marin.weatherjava.models.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IWeatherService {

    @GET("api/location/search")
    Call<List<SearchResult>> findLocationByName(@Query("query") String query);

    @GET("api/location/search")
    Call<List<SearchResult>> findLocationByCoordinates(@Query("lattlong") String coordinates);

    @GET("api/location/location/{id}")
    Call<Location> getLocationWeather(@Path("id") String id);
}
