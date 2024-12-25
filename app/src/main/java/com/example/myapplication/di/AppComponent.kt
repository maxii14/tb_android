package com.example.myapplication.di

import android.app.Activity
import android.content.Context
import com.example.myapplication.di.module.DataModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        PresentationModule::class
    ]
)
interface AppComponent {

    fun inject(activity: Activity)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}