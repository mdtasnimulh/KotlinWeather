package com.tasnim.chowdhury.kotlinweather

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tasnim.chowdhury.kotlinweather.adapter.DailyAdapter
import com.tasnim.chowdhury.kotlinweather.adapter.HourlyAdapter
import com.tasnim.chowdhury.kotlinweather.databinding.ActivityMainBinding
import com.tasnim.chowdhury.kotlinweather.repository.WeatherRepository
import com.tasnim.chowdhury.kotlinweather.utils.apiKey
import com.tasnim.chowdhury.kotlinweather.utils.unit
import com.tasnim.chowdhury.kotlinweather.viewModel.WeatherViewModel
import com.tasnim.chowdhury.kotlinweather.viewModel.WeatherViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    var lat = ""
    var lon = ""

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            WeatherRepository(this.application as Application)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupAdapter()
        setUpDailyAdapter()
        getLocation()

        initData()
        setupObserver()
    }

    private fun initData() {
        binding.currentDateTime.text = getCurrentDate()
    }

    private fun setProgressBarHumidity(humidity: Int){
        val handler = Handler(Looper.getMainLooper())
        binding.humidityProgress.max = 100
        binding.humidityProgress.min = 0

        // Simulate progress updates
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (humidity <= 100) {
                    binding.humidityProgress.progress = humidity
                    handler.postDelayed(this, 300)
                }
            }
        }, 300)
    }

    private fun getCurrentDate(): String{
        val dateFormat = SimpleDateFormat("MMMM dd, yyy", Locale.US)
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }

    private fun setupAdapter() {
        hourlyAdapter = HourlyAdapter()
        binding.hourlyRv.adapter = hourlyAdapter
        binding.hourlyRv.setHasFixedSize(false)
        binding.hourlyRv.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyRv.layoutManager = layoutManager
    }

    private fun setUpDailyAdapter() {
        dailyAdapter = DailyAdapter()
        binding.dailyRv.adapter = dailyAdapter
        binding.dailyRv.setHasFixedSize(false)
        binding.dailyRv.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.dailyRv.layoutManager = layoutManager
    }

    private fun setupObserver() {
        weatherViewModel.apply {
            currentWeatherData.observe(this@MainActivity){
                if (it != null){
                    Log.d("chkNullValueReturn", "${it.temp}")
                    binding.currentTemp.text = "${it.temp.roundToInt()}°"
                    binding.windSpeedTv.text = "${it.windSpeed}km/h"
                    binding.cloudTv.text = "${it.clouds}%"
                    binding.humidityTv.text = "${it.humidity}%"
                    binding.currentCondition.text = "${it.weather[0].description}"

                    binding.humidityValue.text = "${it.humidity}%"
                    setProgressBarHumidity(it.humidity)

                    binding.feelsLikeTv.text = "Feels Like ${it.feelsLike}"
                    binding.uvIndexTv.text = "UV Index ${it.uvi}"

                    for (i in it.weather){
                        if (i.icon == "01d"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.clear_sky_day)
                        }
                        if (i.icon == "01n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.clear_sky_night)
                        }
                        if (i.icon == "02d"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.few_clouds_d)
                        }
                        if (i.icon == "02n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.few_clouds_n)
                        }
                        if (i.icon == "03d" || i.icon == "03n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.scater_clouds)
                        }
                        if (i.icon == "04d" || i.icon == "04n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.broken_clouds)
                        }
                        if (i.icon == "09d"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.rain_d)
                        }
                        if (i.icon == "09n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.rain_n)
                        }
                        if (i.icon == "10d"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.rain_d)
                        }
                        if (i.icon == "10n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.rain_n)
                        }
                        if (i.icon == "11d"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.thunder_d)
                        }
                        if (i.icon == "11n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.thunder_n)
                        }
                        if (i.icon == "13d" || i.icon == "13n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.snow)
                        }
                        if (i.icon == "50d"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.mist_d)
                        }
                        if (i.icon == "50n"){
                            binding.currentWeatherIcon.setImageResource(R.drawable.mist_n)
                        }
                    }
                }
            }

            hourlyWeatherData.observe(this@MainActivity){
                hourlyAdapter.addHourlyItem(it)
            }

            dailyWeatherData.observe(this@MainActivity){
                dailyAdapter.addDailyItem(it)
            }
        }
    }


    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    Log.d("chkLocationD", "$location +++")
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val place = list?.get(0)
                        Log.d("chkLocationD", "$list ---")
                        lat = list?.get(0)?.latitude.toString()
                        lon = list?.get(0)?.longitude.toString()
                        weatherViewModel.getWeatherData(
                            lat = lat,
                            lon = lon,
                            appId = apiKey,
                            unit = unit,
                        )
                        binding.apply {
                            if (place?.subLocality.isNullOrEmpty()) {
                                areaName.text = place?.thoroughfare
                            } else {
                                areaName.text = place?.subLocality
                            }
                        }

                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), permissionId)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}