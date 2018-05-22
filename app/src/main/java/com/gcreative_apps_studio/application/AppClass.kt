package com.gcreative_apps_studio.application

import android.app.Application
import com.gcreative_apps_studio.data.model.MyObjectBox
import io.objectbox.BoxStore

class AppClass : Application() {
    lateinit var boxStore: BoxStore

    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder().androidContext(this).build()
    }
}