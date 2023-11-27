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
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tasnim.chowdhury.kotlinweather.databinding.ActivityMainBinding
import com.tasnim.chowdhury.kotlinweather.repository.WeatherRepository
import com.tasnim.chowdhury.kotlinweather.utils.apiKey
import com.tasnim.chowdhury.kotlinweather.utils.unit
import com.tasnim.chowdhury.kotlinweather.viewModel.WeatherViewModel
import com.tasnim.chowdhury.kotlinweather.viewModel.WeatherViewModelFactory
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

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
        binding.btnLocation.setOnClickListener {
            getLocation()
        }

        setupObserver()
    }

    private fun setupObserver() {
        weatherViewModel.apply {
            currentWeatherData.observe(this@MainActivity){
                if (it != null){
                    Log.d("chkNullValueReturn", "${it.temp}")
                }
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
                        Log.d("chkLocationD", "$list ---")
                        binding.apply {
                            tvLatitude.text = "Latitude\n${list?.get(0)?.latitude}"
                            tvLongitude.text = "Longitude\n${list?.get(0)?.longitude}"
                            tvCountryName.text = "Country Name\n${list?.get(0)?.countryName}"
                            tvLocality.text = "Locality\n${list?.get(0)?.locality}"
                            tvAddress.text = "Address\n${list?.get(0)?.getAddressLine(0)}"
                        }
                        weatherViewModel.getWeatherData(
                            lat = list?.get(0)?.latitude.toString(),
                            lon = list?.get(0)?.longitude.toString(),
                            appId = apiKey,
                            unit = unit,
                        )

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