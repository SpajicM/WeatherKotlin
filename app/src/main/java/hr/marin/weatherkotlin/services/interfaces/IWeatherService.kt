package hr.marin.weatherkotlin.services.interfaces

import hr.marin.weatherkotlin.models.Location
import hr.marin.weatherkotlin.models.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IWeatherService {
    @GET("api/location/search")
    fun findLocationByName(@Query("query") query:String): Call<List<SearchResult>>
    @GET("api/location/search")
    fun findLocationByCoordinates(@Query("lattlong") coordinates:String): Call<List<SearchResult>>
    @GET("api/location/location/{id}")
    fun getLocationWeather(@Path("id") id:Int): Call<Location>
}