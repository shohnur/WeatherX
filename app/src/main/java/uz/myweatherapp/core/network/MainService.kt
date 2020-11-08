package uz.myweatherapp.core.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import uz.myweatherapp.core.model.current.CurrentWeather
import uz.myweatherapp.core.model.daily.DailyData

interface MainService {

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
    ): Single<CurrentWeather>

    @GET("onecall")
    fun getDailyWeekly(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("exlude") exlude: String
    ): Single<DailyData>


}