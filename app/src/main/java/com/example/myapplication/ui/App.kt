package com.example.myapplication.ui

import android.app.Application
import com.example.myapplication.data.db.AppDB

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDB.initDatabase(this)
    }

}