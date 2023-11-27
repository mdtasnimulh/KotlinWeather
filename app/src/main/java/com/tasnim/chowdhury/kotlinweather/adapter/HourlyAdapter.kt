package com.tasnim.chowdhury.kotlinweather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tasnim.chowdhury.kotlinweather.R
import com.tasnim.chowdhury.kotlinweather.databinding.TodayWeatherItemBinding
import com.tasnim.chowdhury.kotlinweather.model.WeatherModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class HourlyAdapter(): RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private var weatherData: List<WeatherModel.Hourly> = listOf()
    var selectedItemPosition = -1

    inner class HourlyViewHolder(private val binding: TodayWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourly: WeatherModel.Hourly, position: Int) {
            binding.hourlyIcon.setImageResource(R.drawable.clouds)
            val formattedTime = convertUnixTimeStampToTime(hourly.dt.toLong())
            binding.hourlyTime.text = formattedTime
            val hourlyTemp = "${hourly.temp.roundToInt()}°"
            binding.hourlyTemp.text = hourlyTemp

            if (position == selectedItemPosition) {
                binding.todayWeatherItem.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.blue)
                binding.hourlyTime.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                binding.hourlyTemp.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            } else {
                binding.todayWeatherItem.backgroundTintList =
                    ContextCompat.getColorStateList(binding.root.context, R.color.lightGrey)
                binding.hourlyTime.setTextColor(ContextCompat.getColor(binding.root.context, R.color.mediumBlack))
                binding.hourlyTemp.setTextColor(ContextCompat.getColor(binding.root.context, R.color.mediumBlack))
            }

            binding.root.setOnClickListener {
                handleItemClick(position)
                Log.d("chkIcon", "${hourly.weather[0].icon}")
            }

            for (i in hourly.weather){
                if (i.icon == "01d"){
                    binding.hourlyIcon.setImageResource(R.drawable.clear_sky_day)
                }
                if (i.icon == "01n"){
                    binding.hourlyIcon.setImageResource(R.drawable.clear_sky_night)
                }
                if (i.icon == "02d"){
                    binding.hourlyIcon.setImageResource(R.drawable.few_clouds_d)
                }
                if (i.icon == "02n"){
                    binding.hourlyIcon.setImageResource(R.drawable.few_clouds_n)
                }
                if (i.icon == "03d" || i.icon == "03n"){
                    binding.hourlyIcon.setImageResource(R.drawable.scater_clouds)
                }
                if (i.icon == "04d" || i.icon == "04n"){
                    binding.hourlyIcon.setImageResource(R.drawable.broken_clouds)
                }
                if (i.icon == "09d"){
                    binding.hourlyIcon.setImageResource(R.drawable.rain_d)
                }
                if (i.icon == "09n"){
                    binding.hourlyIcon.setImageResource(R.drawable.rain_n)
                }
                if (i.icon == "10d"){
                    binding.hourlyIcon.setImageResource(R.drawable.rain_d)
                }
                if (i.icon == "10n"){
                    binding.hourlyIcon.setImageResource(R.drawable.rain_n)
                }
                if (i.icon == "11d"){
                    binding.hourlyIcon.setImageResource(R.drawable.thunder_d)
                }
                if (i.icon == "11n"){
                    binding.hourlyIcon.setImageResource(R.drawable.thunder_n)
                }
                if (i.icon == "13d" || i.icon == "13n"){
                    binding.hourlyIcon.setImageResource(R.drawable.snow)
                }
                if (i.icon == "50d"){
                    binding.hourlyIcon.setImageResource(R.drawable.mist_d)
                }
                if (i.icon == "50n"){
                    binding.hourlyIcon.setImageResource(R.drawable.mist_n)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            TodayWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return if (weatherData.size > 12){
            24
        }else {
            weatherData.size
        }
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(weatherData[position], position)
    }

    fun addHourlyItem(item: List<WeatherModel.Hourly>){
        weatherData = item
        notifyDataSetChanged()
    }

    private fun handleItemClick(position: Int) {
        selectedItemPosition = if (selectedItemPosition == position) { -1 } else { position }
        notifyDataSetChanged()
    }

    fun convertUnixTimeStampToTime(unixTimeStamp: Long): String {
        val dateFormat = SimpleDateFormat("h:mm a", Locale.US)
        dateFormat.timeZone = TimeZone.getDefault()
        val dateTime = Date(unixTimeStamp * 1000)
        return dateFormat.format(dateTime)
    }
}