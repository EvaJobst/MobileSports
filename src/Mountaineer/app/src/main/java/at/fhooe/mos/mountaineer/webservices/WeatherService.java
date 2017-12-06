package at.fhooe.mos.mountaineer.webservices;

import at.fhooe.mos.mountaineer.model.weather.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Eva on 30.11.2017.
 */

public interface WeatherService {
    @GET("weather")
    Call<Weather> fetch(
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("units") String units,
            @Query("APPID") String key);
}
