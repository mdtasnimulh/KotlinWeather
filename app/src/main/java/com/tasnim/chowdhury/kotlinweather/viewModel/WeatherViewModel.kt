package com.tasnim.chowdhury.kotlinweather.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasnim.chowdhury.kotlinweather.model.WeatherModel
import com.tasnim.chowdhury.kotlinweather.repository.WeatherRepository
import com.tasnim.chowdhury.kotlinweather.utils.apiKey
import com.tasnim.chowdhury.kotlinweather.utils.unit
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel(val repository: WeatherRepository) : ViewModel()  {

    private val _currentWeatherData = MutableLiveData<WeatherModel.Current?>()
    val currentWeatherData: LiveData<WeatherModel.Current?> = _currentWeatherData

    private val _dailyWeatherData = MutableLiveData<List<WeatherModel.Daily>>()
    val dailyWeatherData: LiveData<List<WeatherModel.Daily>> = _dailyWeatherData

    private val _hourlyWeatherData = MutableLiveData<List<WeatherModel.Hourly>>()
    val hourlyWeatherData: LiveData<List<WeatherModel.Hourly>> = _hourlyWeatherData

    private val _dataLoading = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getWeatherData(
        lat: String?,
        lon: String?,
        appId: String?,
        unit: String?,
    ){
        viewModelScope.launch {
            Log.d("chkNoValue", "1")
            _dataLoading.value = true
            try {
                Log.d("chkNoValue", "2")
                val response = repository.getWeatherData(
                    lat = lat,
                    lon = lon,
                    appId = appId,
                    unit = unit,
                )
                Log.d("chkNoValue", "3 ${response.body()}")
                _dataLoading.value = false
                if (response.isSuccessful){
                    Log.d("chkNoValue", "4 ${response.body()}")
                    _currentWeatherData.value = response.body()?.current
                    _dailyWeatherData.value = response.body()?.daily
                    _hourlyWeatherData.value = response.body()?.hourly
                }else {

                    Log.d("chkNoValue", "5")
                    _dataLoading.value = false
                }
            }catch (e: Exception){
                Log.d("chkNoValue", "6 ${e.message}")
                _dataLoading.value = false
            }
        }
    }

}