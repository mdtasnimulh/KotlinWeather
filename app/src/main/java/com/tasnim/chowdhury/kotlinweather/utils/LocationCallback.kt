package com.tasnim.chowdhury.kotlinweather.utils

interface latLonCallback {
    fun onLocationReceived(latitude: String, longitude: String)
}