package uz.myweatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.mainlayout.*
import uz.myweatherapp.R
import uz.myweatherapp.core.adapter.RVAdapter
import uz.myweatherapp.core.db.Database
import uz.myweatherapp.core.model.Data
import uz.myweatherapp.core.model.current.CurrentWeather
import uz.myweatherapp.core.model.daily.Daily
import uz.myweatherapp.core.model.daily.DailyData
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainPresenter
    var lat: String? = null
    var lon: String? = null
    var locationGPS: Location? = null
    private val adapter = RVAdapter()
    private var lastUpdateS: String? = null

    private var d1: String? = null
    private var d2: String? = null
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainlayout)

        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationGPS =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        getLocation()
        presenter.loadAll(lat!!, lon!!, "daily,weekly")


        swipe.setOnRefreshListener {
            getLocation()
            presenter.loadAll(lat!!, lon!!, "daily,weekly")
            swipe.isRefreshing = false
        }

        adapter.onItemSelected = object : RVAdapter.OnItemSelected {
            override fun onItemSelected(d: Daily) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)

                val g = Gson()
                val s = g.toJson(d)

                intent.putExtra("data", s)
                startActivity(intent)
            }
        }


    }

    private fun getLocation() {
        if (locationGPS != null) {
            lat = locationGPS!!.latitude.toString()
            lon = locationGPS!!.longitude.toString()
        } else {
            Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun setCurrentWeather(data: CurrentWeather) {

        Log.d("tagtag", data.toString())

        d1 = Gson().toJson(data).toString()
        ++i
        city_name.text = String.format("%s, %s", data.name, data.sys?.country)

        change()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        lastUpdate.text = String.format("Last update: %s", currentDate)
        val s = data.weather!![0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/${s}@2x.png").into(condition)
        temp.text = String.format("%s°C", (data.main?.temp!! - 273.15).toString())
        feelsLike.text =
            String.format("Feels Like %s°C", String.format("%.1f", (data.main.feelsLike - 273.15)))
        humidity.text = String.format("Humidity: %s", data.main.humidity.toString())
        pressure.text = String.format("Pressure: %s PA", (data.main.pressure!! * 100).toString())

        val sunset = data.sys!!.sunset!!.times(1000).toLong() + 10800000
        val sunrise = data.sys.sunrise!!.times(1000).toLong() + 10800000

        val ss = Date(sunset)
        val sr = Date(sunrise)
        val df = SimpleDateFormat("hh:mm")

        sunSet.text = String.format("Sunset: %sPM", df.format(ss))
        sunRise.text = String.format("Sunrise: %sAM", df.format(sr))

    }

    override fun setDaily(data: DailyData) {

        Log.d("tagtag", data.toString())



        d2 = Gson().toJson(data).toString()

        ++i
        change()

        Log.i("TTT", data.toString())

        adapter.data = data.daily!!
        rv1.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun change() {
        if (i % 2 == 0) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            lastUpdateS = String.format("Last update: %s", currentDate)
            Database.getDB().dao().clear()
            Database.getDB().dao().insert(Data(0, d1, d2, lastUpdateS))
        }
    }


    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun setError(t: String) {
        Log.d("error", "load")
        val d = Database.getDB().dao().getData()
        if (d != null && d.isNotEmpty()) {
            Toast.makeText(this, t, Toast.LENGTH_SHORT).show()
            val d1: CurrentWeather? = Gson().fromJson(d[0].current, CurrentWeather::class.java)
            val d2: DailyData? = Gson().fromJson(d[0].daily, DailyData::class.java)
            d1?.let { current ->
                setCurrentWeatherOffline(current)
                d2?.let { daily ->
                    setDailyOffline(daily)
                }
            }
            lastUpdate.text = d[0].lastUpdate
        } else {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setDailyOffline(data: DailyData) {
        adapter.data = data.daily!!
        rv1.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setCurrentWeatherOffline(data: CurrentWeather) {

        val s = data.weather!![0].icon
        Glide.with(this).load("http://openweathermap.org/img/wn/${s}@2x.png").into(condition)
        city_name.text = String.format("%s, %s", data.name, data.sys?.country)
        temp.text = String.format("%s°C", (data.main?.temp!! - 273.15).toString())
        feelsLike.text =
            String.format("Feels Like %s°C", String.format("%.1f", (data.main.feelsLike - 273.15)))
        humidity.text = String.format("Humidity: %s", data.main.humidity.toString())
        pressure.text = String.format("Pressure: %s PA", (data.main.pressure!! * 100).toString())

        val sunset = data.sys!!.sunset!!.times(1000).toLong() + 10800000
        val sunrise = data.sys.sunrise!!.times(1000).toLong() + 10800000

        val ss = Date(sunset)
        val sr = Date(sunrise)
        val df = SimpleDateFormat("hh:mm")

        sunSet.text = String.format("Sunset: %sPM", df.format(ss))
        sunRise.text = String.format("Sunrise: %sAM", df.format(sr))

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onStop() {
        presenter.onCancel()
        super.onStop()
    }


}