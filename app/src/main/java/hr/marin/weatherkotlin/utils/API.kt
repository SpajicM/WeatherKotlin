package hr.marin.weatherkotlin.utils

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object API {
    private const val rootUrl = "https://www.metaweather.com/"
    private const val IMAGES_PATH = "/static/img/weather/png/"

    val retrofit: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(rootUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }

    val imagesPath:String
        get() {
            return rootUrl + IMAGES_PATH
        }
}