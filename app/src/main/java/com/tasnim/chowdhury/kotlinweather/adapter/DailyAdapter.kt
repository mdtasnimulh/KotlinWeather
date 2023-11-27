package com.tasnim.chowdhury.kotlinweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasnim.chowdhury.kotlinweather.R
import com.tasnim.chowdhury.kotlinweather.databinding.DailyWeatherItemBinding
import com.tasnim.chowdhury.kotlinweather.model.WeatherModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class DailyAdapter(): RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    private var weatherData: List<WeatherModel.Daily> = listOf()

    inner class DailyViewHolder(val binding: DailyWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: WeatherModel.Daily, position: Int) {
            binding.dailyIcon.setImageResource(R.drawable.clouds)
            val formattedTime = convertUnixTimeStampToDay(daily.dt.toLong())
            binding.dailyDay.text = formattedTime
            val dailyTemp = "${daily.temp.max.roundToInt()}°/${daily.temp.min.roundToInt()}°"
            binding.dailyMinMax.text = dailyTemp

            if (position == 6){
                binding.viewOne.visibility = View.GONE
            }else {
                binding.viewOne.visibility = View.VISIBLE
            }

            for (i in daily.weather){
                if (i.icon == "01d"){
                    binding.dailyIcon.setImageResource(R.drawable.clear_sky_day)
                }
                if (i.icon == "01n"){
                    binding.dailyIcon.setImageResource(R.drawable.clear_sky_night)
                }
                if (i.icon == "02d"){
                    binding.dailyIcon.setImageResource(R.drawable.few_clouds_d)
                }
                if (i.icon == "02n"){
                    binding.dailyIcon.setImageResource(R.drawable.few_clouds_n)
                }
                if (i.icon == "03d" || i.icon == "03n"){
                    binding.dailyIcon.setImageResource(R.drawable.scater_clouds)
                }
                if (i.icon == "04d" || i.icon == "04n"){
                    binding.dailyIcon.setImageResource(R.drawable.broken_clouds)
                }
                if (i.icon == "09d"){
                    binding.dailyIcon.setImageResource(R.drawable.rain_d)
                }
                if (i.icon == "09n"){
                    binding.dailyIcon.setImageResource(R.drawable.rain_n)
                }
                if (i.icon == "10d"){
                    binding.dailyIcon.setImageResource(R.drawable.rain_d)
                }
                if (i.icon == "10n"){
                    binding.dailyIcon.setImageResource(R.drawable.rain_n)
                }
                if (i.icon == "11d"){
                    binding.dailyIcon.setImageResource(R.drawable.thunder_d)
                }
                if (i.icon == "11n"){
                    binding.dailyIcon.setImageResource(R.drawable.thunder_n)
                }
                if (i.icon == "13d" || i.icon == "13n"){
                    binding.dailyIcon.setImageResource(R.drawable.snow)
                }
                if (i.icon == "50d"){
                    binding.dailyIcon.setImageResource(R.drawable.mist_d)
                }
                if (i.icon == "50n"){
                    binding.dailyIcon.setImageResource(R.drawable.mist_n)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            DailyWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return if (weatherData.size > 7){
            7
        }else {
            weatherData.size
        }
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(weatherData[position], position)
    }

    fun addDailyItem(item: List<WeatherModel.Daily>){
        weatherData = item
        notifyDataSetChanged()
    }

    fun convertUnixTimeStampToDay(unixTimeStamp: Long): String {
        val dateFormat = SimpleDateFormat("EEE", Locale.US)
        dateFormat.timeZone = TimeZone.getDefault()
        val dateTime = Date(unixTimeStamp * 1000)
        return dateFormat.format(dateTime)
    }

}