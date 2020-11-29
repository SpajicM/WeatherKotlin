package hr.marin.weatherkotlin.services

import hr.marin.weatherkotlin.models.Location
import hr.marin.weatherkotlin.models.SearchResult
import hr.marin.weatherkotlin.services.interfaces.IWeatherService
import hr.marin.weatherkotlin.services.listeners.IWeatherServiceListener
import hr.marin.weatherkotlin.utils.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherService {
    private val service = API.retrofit.create(IWeatherService::class.java)

    fun findLocationByName(name:String, handleWeatherService:IWeatherServiceListener<List<SearchResult>>) {
        val call = service.findLocationByName(name)

        call.enqueue(object:Callback<List<SearchResult>> {
            override fun onResponse( call:Call<List<SearchResult>>,  response:Response<List<SearchResult>>) {
                val searchResults = response.body()
                if (searchResults != null) {
                    handleWeatherService.onSuccess(searchResults)
                }
            }
            override fun onFailure( call:Call<List<SearchResult>>,  t:Throwable) {
                handleWeatherService.onFailure(t)
            }
        })
    }
    fun findLocationByCoordinates(latitude:Double, longitude:Double, handleWeatherService:IWeatherServiceListener<List<SearchResult>>) {
        val joinedCoordinates = "$latitude,$longitude"

        val call = service.findLocationByCoordinates(joinedCoordinates)

        call.enqueue(object:Callback<List<SearchResult>> {
            override fun onResponse( call:Call<List<SearchResult>>,  response:Response<List<SearchResult>>) {
                val searchResults = response.body()

                if (searchResults != null) {
                    handleWeatherService.onSuccess(searchResults)
                }
            }
            override fun onFailure( call:Call<List<SearchResult>>,  t:Throwable) {
                handleWeatherService.onFailure(t)
            }
        })
    }

    fun getLocationWeather(woeId:Int, handleWeatherService:IWeatherServiceListener<Location>) {
        val call = service.getLocationWeather(woeId)

        call.enqueue(object:Callback<Location> {
           override fun onResponse( call:Call<Location>,  response:Response<Location>) {
                val locationWeather = response.body()
               if (locationWeather != null) {
                   handleWeatherService.onSuccess(locationWeather)
               }
            }

            override fun onFailure( call:Call<Location>,  t:Throwable) {
                handleWeatherService.onFailure(t)
            }
        })
    }
}