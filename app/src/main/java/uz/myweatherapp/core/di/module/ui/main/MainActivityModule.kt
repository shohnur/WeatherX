package uz.myweatherapp.core.di.module.ui.main

import dagger.Binds
import dagger.Module
import uz.myweatherapp.ui.MainActivity
import uz.myweatherapp.ui.MainContract

@Module
abstract class MainActivityModule {

    @Binds
    abstract fun bindView(activity: MainActivity):MainContract.View

}