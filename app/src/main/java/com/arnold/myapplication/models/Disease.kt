package com.arnold.myapplication.models

data class DiseaseItem(
    val id: Int,
    val name: String,
    val affectedPlants: String,
    val imageRes: Int,
    val description: String,
    val treatment: String,
    val prevention: String
)