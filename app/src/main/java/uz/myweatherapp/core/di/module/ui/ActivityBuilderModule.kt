package uz.myweatherapp.core.di.module.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import uz.myweatherapp.core.di.module.ui.main.MainActivityModule
import uz.myweatherapp.ui.MainActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity

}