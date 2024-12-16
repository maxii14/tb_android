package com.example.myapplication.presentation

import android.app.Application
import com.example.myapplication.data.datasource.local.AppDB

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDB.initDatabase(this)
    }

}