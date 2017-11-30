package at.fhooe.mos.mountaineer.sensors.weather;

import at.fhooe.mos.mountaineer.model.Weather;
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
            @Query("units") String unit,
            @Query("APPID") String key);
}
