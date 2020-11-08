package uz.myweatherapp.core.di.module.network

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import uz.myweatherapp.core.utils.appid
import javax.inject.Singleton

@Module
abstract class OkHttpModule {

    companion object {

        @Provides
        @Singleton
        fun okHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient {
            return OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(interceptor())
                .build()
        }

        //https://api.
        @Provides
        @Singleton
        fun interceptor():Interceptor{
            return Interceptor {
                val request=it.request()
                val url=request.url.newBuilder().addQueryParameter("appid", appid).build()

                it.proceed(request.newBuilder().url(url).build())
            }
        }



        @Provides
        @Singleton
        fun httpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
                Log.d(
                    "TAG",
                    message
                )
            }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }

    }

}