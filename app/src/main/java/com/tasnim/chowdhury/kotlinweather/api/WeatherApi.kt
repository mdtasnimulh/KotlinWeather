package com.tasnim.chowdhury.kotlinweather.api

import com.google.gson.GsonBuilder
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tasnim.chowdhury.kotlinweather.model.WeatherModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface WeatherApi{

    @GET("onecall?")
    suspend fun getWeatherData(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appid: String?,
        @Query("units") units: String?,
    ) : Response<WeatherModel>

    companion object RetrofitClient {
        private const val BASE_URL = "https://api.openweathermap.org/data/3.0/"

        @Volatile
        private var retrofit: Retrofit? = null

        @Synchronized
        fun create(): WeatherApi {

            retrofit ?: synchronized(this) {
                retrofit = buildRetrofit()
            }

            return retrofit?.create(WeatherApi::class.java)!!
        }

        private fun buildRetrofit(): Retrofit {

            val gson = GsonBuilder().setLenient().create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(OkHttpProfilerInterceptor())
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder().apply {
                baseUrl(BASE_URL.trim())
                addConverterFactory(GsonConverterFactory.create(gson)).addConverterFactory(
                    MoshiConverterFactory.create(moshi)
                )
                client(okHttpClient)
            }.build()
        }
    }

}