package com.example.myapplication.presentation

import android.app.Application
import com.example.myapplication.data.datasource.local.AppDB
import com.example.myapplication.di.AppComponent
import com.example.myapplication.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        AppDB.initDatabase(this)
    }

}