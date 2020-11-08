package uz.myweatherapp.core.di.module.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.myweatherapp.core.utils.baseUrl
import uz.myweatherapp.core.utils.baseUrlOnMapCall
import uz.myweatherapp.ui.MainPresenter
import javax.inject.Singleton

@Module(
    includes = [
        OkHttpModule::class
    ]
)
abstract class RetrofitModule {



    companion object {

        @Singleton
        @Provides
        fun gson():Gson{
            return GsonBuilder().create()
        }

        @Singleton
        @Provides
        fun rxJava3CallAdapterFactory():RxJava3CallAdapterFactory{
            return RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io())
        }

        @Provides
        @Singleton
        fun gsonConverterFactory(gson: Gson):GsonConverterFactory{
            return GsonConverterFactory.create(gson)
        }

        @Provides
        @Singleton
        fun retrofit(
            okHttpClient: OkHttpClient,
            gsonConverterFactory: GsonConverterFactory,
            callAdapterFactory: RxJava3CallAdapterFactory
        ): Retrofit {

            var b= baseUrl
            object :MainPresenter.OnMapCall{
                override fun onMapCall() {
                    b= baseUrlOnMapCall
                }
            }

            return Retrofit.Builder().baseUrl(b)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
        }



    }

}