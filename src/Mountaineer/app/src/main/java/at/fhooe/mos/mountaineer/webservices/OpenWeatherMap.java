package at.fhooe.mos.mountaineer.webservices;

import at.fhooe.mos.mountaineer.model.weather.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stefan on 06.12.2017.
 */

public class OpenWeatherMap {
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "17b9da43ec1ff13f7f3b0b4ba8e21bb6";
    private static final String UNITS = "metric";

    public static void fetchWeatherForLocation(double latitude, double longitude, Callback<Weather> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<Weather> data = weatherService.fetch(String.valueOf(latitude), String.valueOf(longitude), UNITS, API_KEY);
        data.enqueue(callback);
    }
}
