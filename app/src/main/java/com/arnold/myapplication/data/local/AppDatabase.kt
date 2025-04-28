package com.arnold.myapplication.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arnold.myapplication.data.local.entities.User
import com.arnold.myapplication.data.local.entities.DiseaseAnalysis
import com.arnold.myapplication.data.local.dao.UserDao
import com.arnold.myapplication.data.local.dao.DiseaseDao

@Database(
    entities = [User::class, DiseaseAnalysis::class],
    version = 3,
    exportSchema = true,  // This is crucial
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // ... rest of your code ...
    abstract fun userDao(): UserDao
    abstract fun diseaseDao(): DiseaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shambatech_db"
                )
                    .addCallback(databaseCallback)
                    .fallbackToDestructiveMigration() // Optional: handles migration failures
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Initial data seeding if needed
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Enable foreign key constraints
                db.execSQL("PRAGMA foreign_keys = ON")
            }
        }
    }
}