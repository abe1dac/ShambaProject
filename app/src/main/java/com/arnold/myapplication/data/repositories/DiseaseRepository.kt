package com.arnold.myapplication.data.repositories

import com.arnold.myapplication.data.local.dao.DiseaseDao
import com.arnold.myapplication.data.local.entities.DiseaseAnalysis
import kotlinx.coroutines.flow.Flow

class DiseaseRepository(private val diseaseDao: DiseaseDao) {
    fun getAnalysesForUser(userId: Long): Flow<List<DiseaseAnalysis>> {
        return diseaseDao.getAnalysesForUser(userId)
    }

    suspend fun saveAnalysis(
        userId: Long,
        plantType: String,
        diseaseName: String,
        confidence: Float,
        imageUri: String,
        treatment: String
    ): Long {
        val analysis = DiseaseAnalysis(
            userId = userId,
            plantType = plantType,
            diseaseName = diseaseName,
            confidence = confidence,
            imageUri = imageUri,
      //  treatmentSuggestions = treatment
        )
        return diseaseDao.insertAnalysis(analysis)
    }
}