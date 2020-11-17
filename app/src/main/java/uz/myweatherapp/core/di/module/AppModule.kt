package uz.myweatherapp.core.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import uz.myweatherapp.core.di.module.network.RetrofitModule
import uz.myweatherapp.core.di.module.ui.ActivityBuilderModule
import javax.inject.Singleton

@Module(
    includes = [
        RetrofitModule::class,
        ActivityBuilderModule::class
    ]
)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun context(application: Application): Context
}