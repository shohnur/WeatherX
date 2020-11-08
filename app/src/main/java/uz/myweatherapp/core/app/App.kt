package uz.myweatherapp.core.app

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import uz.myweatherapp.core.db.Database
import uz.myweatherapp.core.di.component.AppComponent
import uz.myweatherapp.core.di.component.DaggerAppComponent

class App : DaggerApplication() {

    var appComponent:AppComponent?=null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        appComponent=DaggerAppComponent.builder().application(this).build()
        appComponent!!.inject(this)

        return appComponent!!
    }

    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }
}