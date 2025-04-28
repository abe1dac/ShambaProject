package com.arnold.myapplication.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "disease_analyses",
    indices = [Index("user_id")],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DiseaseAnalysis(
    @PrimaryKey(autoGenerate = true)
    val analysisId: Long = 0,

    @ColumnInfo(name = "user_id")  // Must match the query
    val userId: Long,


    val plantType: String,

    val diseaseName: String,

    val confidence: Float,

    val imageUri: String,

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val analyzedAt: Long = System.currentTimeMillis()
)