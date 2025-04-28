package com.arnold.myapplication

import android.app.Application
import androidx.room.Room
import com.arnold.myapplication.data.local.AppDatabase
import kotlin.jvm.java

class ShambaTechApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "shambatech-db"
        ).build()
    }
}