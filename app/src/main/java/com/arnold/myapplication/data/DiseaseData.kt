package com.arnold.myapplication.data

import com.arnold.myapplication.models.DiseaseItem
import com.arnold.myapplication.R

fun getDiseaseItems(): List<DiseaseItem> {
    return listOf(
        DiseaseItem(
            id = 1,
            name = "Cinnamon spot Leaf",
            affectedPlants = "potato, sukuma & spinach",
            imageRes = R.drawable.leaf1,
            description = "Fungal disease causing cinnamon-colored spots...",
            treatment = "Apply fungicide and remove infected leaves...",
            prevention = "Rotate crops and maintain proper spacing..."
        ),
        DiseaseItem(
            id = 2,
            name = "Early Blight",
            affectedPlants = "tomato, potato",
            imageRes = R.drawable.leaf1,
            description = "Causes dark concentric spots on leaves...",
            treatment = "Use copper-based fungicides...",
            prevention = "Avoid overhead watering..."
        ),
                DiseaseItem(
                id = 2,
        name = "Early Blight",
        affectedPlants = "tomato, potato",
        imageRes = R.drawable.leaf1,
        description = "Causes dark concentric spots on leaves...",
        treatment = "Use copper-based fungicides...",
        prevention = "Avoid overhead watering..."
    )
    )
}

fun getDiseaseById(id: Int): DiseaseItem {
    return getDiseaseItems().first { it.id == id }
}