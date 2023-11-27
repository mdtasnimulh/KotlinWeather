package com.tasnim.chowdhury.kotlinweather.repository

import android.app.Application
import com.tasnim.chowdhury.kotlinweather.api.WeatherApi
import com.tasnim.chowdhury.kotlinweather.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class WeatherRepository(application: Application) {

    suspend fun getWeatherData(
        lat: String?,
        lon: String?,
        appId: String?,
        unit: String?,
    ): Response<WeatherModel> {
        return withContext(Dispatchers.IO){
            WeatherApi.create().getWeatherData(
                lat = lat,
                lon= lon,
                appid = appId,
                units = unit,
            )
        }
    }

}