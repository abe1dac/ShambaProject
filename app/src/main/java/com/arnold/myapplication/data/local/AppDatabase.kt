package com.arnold.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.arnold.myapplication.data.local.entities.User
import com.arnold.myapplication.data.local.entities.DiseaseAnalysis
import com.arnold.myapplication.data.local.dao.UserDao
import com.arnold.myapplication.data.local.dao.DiseaseDao

@Database(
    entities = [User::class, DiseaseAnalysis::class],
    version = 3,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Assume version 1 had only User table, and version 2 added DiseaseAnalysis
                database.execSQL(
                    """
                    CREATE TABLE DiseaseAnalysis (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        diseaseName TEXT NOT NULL,
                        analysisDate INTEGER NOT NULL,
                        result TEXT NOT NULL,
                        FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE
                    )
                """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example: Add a new column for version 3
                // Adjust based on actual changes
                database.execSQL("ALTER TABLE DiseaseAnalysis ADD COLUMN confidenceScore REAL")
            }
        }

        private val databaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Initial data seeding if needed
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }

}
// Enable foreign key constraints