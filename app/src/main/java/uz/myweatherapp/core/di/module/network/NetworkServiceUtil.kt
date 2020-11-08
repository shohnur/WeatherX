package uz.myweatherapp.core.di.module.network

import retrofit2.Retrofit
import javax.inject.Inject

class NetworkServiceUtil @Inject constructor(
    retrofit: Retrofit
) {
    private var retrofit: Retrofit? = null

    init {
        this.retrofit = retrofit
    }

    fun <T> getService(aClass: Class<T>): T {
        return retrofit!!.create(aClass)
    }

}