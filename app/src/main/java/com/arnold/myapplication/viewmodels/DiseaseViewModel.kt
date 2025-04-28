package com.arnold.myapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.arnold.myapplication.data.repositories.DiseaseRepository

class DiseaseViewModel(private val repository: DiseaseRepository) : ViewModel() {
    fun getAnalyses(userId: Long) = repository.getAnalysesForUser(userId)

    suspend fun saveAnalysis(
        userId: Long,
        plantType: String,
        diseaseName: String,
        confidence: Float,
        imageUri: String,
        treatment: String
    ) {
        repository.saveAnalysis(userId, plantType, diseaseName, confidence, imageUri, treatment)
    }
}