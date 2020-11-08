package uz.myweatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.mainlayout.feelsLike
import kotlinx.android.synthetic.main.mainlayout.humidity
import kotlinx.android.synthetic.main.mainlayout.pressure
import kotlinx.android.synthetic.main.mainlayout.sunRise
import kotlinx.android.synthetic.main.mainlayout.sunSet
import kotlinx.android.synthetic.main.mainlayout.temp
import uz.myweatherapp.R
import uz.myweatherapp.core.model.daily.Daily
import java.text.SimpleDateFormat
import java.util.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val d = intent.getStringExtra("data")!!

        val data = Gson().fromJson(d, Daily::class.java)

        val s = data.weather!![0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/${s}@2x.png").into(conditionX)

        temp.text = String.format("%s°C", String.format("%.1f", data.temp!!.day!! - 273.15))
        feelsLike.text = String.format(
            "Feels Like %s°C",
            String.format("%.1f", (data.feelsLike!!.day!! - 273.15))
        )
        humidity.text = String.format("Humidity: %s", data.humidity.toString())
        pressure.text = String.format("Pressure: %s PA", (data.pressure!! * 100).toString())

        val sunset = data.sunset!!.times(1000).toLong() + 10800000
        val sunrise = data.sunrise!!.times(1000).toLong() + 10800000

        val ss = Date(sunset)
        val sr = Date(sunrise)
        val df = SimpleDateFormat("hh:mm")

        sunSet.text = String.format("Sunset: %sPM", df.format(ss))
        sunRise.text = String.format("Sunrise: %sAM", df.format(sr))

    }
}