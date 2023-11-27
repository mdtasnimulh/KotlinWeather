package com.tasnim.chowdhury.kotlinweather.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tasnim.chowdhury.kotlinweather.repository.WeatherRepository

class WeatherViewModelFactory(private val repository: WeatherRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}