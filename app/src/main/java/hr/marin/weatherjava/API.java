package hr.marin.weatherjava;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if(retrofit == null) {
            Gson gson = new GsonBuilder().create();

            String rootUrl = "https://www.metaweather.com/";
            retrofit = new Retrofit.Builder()
                    .baseUrl(rootUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
