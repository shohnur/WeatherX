package uz.myweatherapp.ui

import uz.myweatherapp.core.model.current.CurrentWeather
import uz.myweatherapp.core.model.daily.DailyData

interface MainContract {

    interface View {
        fun setCurrentWeather(data: CurrentWeather)
        fun setDaily(data: DailyData)
        fun showProgress()
        fun hideProgress()
        fun setError(t:String)
    }

    interface Presenter {

        fun onDestroy()
        fun onCancel()
        fun loadAll(lat: String, lon: String, exclude: String)
    }

}