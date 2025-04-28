package com.arnold.myapplication.data.local.dao

import androidx.room.*
import com.arnold.myapplication.data.local.entities.DiseaseAnalysis
import kotlinx.coroutines.flow.Flow

@Dao
interface DiseaseDao {
    @Insert
    suspend fun insertAnalysis(analysis: DiseaseAnalysis): Long

    @Query("SELECT * FROM disease_analyses WHERE user_id = :userId ORDER BY analyzedAt DESC")
    fun getAnalysesForUser(userId: Long): Flow<List<DiseaseAnalysis>>

    @Delete
    suspend fun deleteAnalysis(analysis: DiseaseAnalysis)
}