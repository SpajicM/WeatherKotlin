package hr.marin.weatherjava.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private final static String ROOT_URL = "https://www.metaweather.com/";
    private final static String IMAGES_PATH = "/static/img/weather/png/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            Gson gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(getRootUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static String getRootUrl() {
        return ROOT_URL;
    }

    public static String getImagesPath() {
        return ROOT_URL + IMAGES_PATH;
    }
}
