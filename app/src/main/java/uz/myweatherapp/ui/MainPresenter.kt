package uz.myweatherapp.ui

import android.media.Image
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import uz.myweatherapp.core.di.module.network.NetworkServiceUtil
import uz.myweatherapp.core.model.current.CurrentWeather
import uz.myweatherapp.core.model.daily.DailyData
import uz.myweatherapp.core.network.MainService
import javax.inject.Inject

class MainPresenter @Inject constructor(
    serviceUtil: NetworkServiceUtil,
    var view: MainContract.View
) : MainContract.Presenter {

    var service = serviceUtil.getService(MainService::class.java)

    var disposable: CompositeDisposable? = null

    interface OnMapCall {
        fun onMapCall()
    }

    var onMapCall: OnMapCall? = null

    private fun loadCurrentWeather(lat: String, lon: String) {
        view.showProgress()
        Log.i("TAG", "Loading")
        if (disposable == null)
            disposable = CompositeDisposable()

        val response = service.getCurrentWeather(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view.hideProgress()
            }

        disposable!!.add(response.subscribeWith(object :
            DisposableSingleObserver<CurrentWeather>() {
            override fun onSuccess(t: CurrentWeather) {
                view.setCurrentWeather(t)
            }

            override fun onError(e: Throwable?) {
                view.setError(e.toString())
            }
        }))

    }




    private fun loadDailyWeekly(lat: String, lon: String, exclude: String) {
        if (disposable == null)
            disposable = CompositeDisposable()
        val response = service.getDailyWeekly(lat, lon, exclude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


        Log.i("TTT","Loading$response")

        disposable!!.add(response.subscribeWith(object :
            DisposableSingleObserver<DailyData>() {
            override fun onSuccess(t: DailyData) {
                view.setDaily(t)
            }

            override fun onError(e: Throwable?) {
                view.setError(e.toString())
            }
        }))

    }

    override fun onDestroy() {
        disposable?.let {
            if (!it.isDisposed) {
                it.clear()
            }
        }
    }

    override fun onCancel() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun loadAll(lat: String, lon: String, exclude: String) {
        loadDailyWeekly(lat, lon, exclude)
        loadCurrentWeather(lat, lon)
    }
}