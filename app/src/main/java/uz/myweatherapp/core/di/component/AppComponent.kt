package uz.myweatherapp.core.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import uz.myweatherapp.core.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {


    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(app:Application):Builder

        fun build():AppComponent
    }

}