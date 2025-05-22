package com.arnold.myapplication.data

import com.arnold.myapplication.models.DiseaseItem
import com.arnold.myapplication.R

fun getDiseaseItems(): List<DiseaseItem> {
    return listOf(
        DiseaseItem(
            id = 1,
            name = "Cinnamon spot Leaf",
            affectedPlants = "potato, sukuma & spinach",
            imageRes = R.drawable.disss4,
            description = "Fungal disease causing cinnamon-colored spots...",
            treatment = "Apply fungicide and remove infected leaves...",
            prevention = "Rotate crops and maintain proper spacing..."
        ),
        DiseaseItem(
            id = 2,
            name = "late Blight",
            affectedPlants = "tomato, potato",
            imageRes = R.drawable.disss5,
            description = "Causes dark concentric spots on leaves...",
            treatment = "Use copper-based fungicides...",
            prevention = "Avoid overhead watering..."
        ),
                DiseaseItem(
                id = 2,
        name = "Early Blight",
        affectedPlants = "tomato, potato",
        imageRes = R.drawable.disss6,
        description = "Causes dark concentric spots on leaves...",
        treatment = "Use copper-based fungicides...",
        prevention = "Avoid overhead watering..."
    ),
                        DiseaseItem(
                        id = 2,
        name = "Rust in beans ",
        affectedPlants = "tomato, potato",
        imageRes = R.drawable.disss9,
        description = "A fungal disease caused mainly by Uromyces appendiculatus.",
        treatment = "Use copper-based fungicides...",
        prevention = "Avoid overhead watering..."
    ),

                                DiseaseItem(
                                id = 2,
        name = "Black rot",
        affectedPlants = "tomato, potato",
        imageRes = R.drawable.disss3,
        description = "Caused by the bacterium Xanthomonas campestris pv. campestris.",
        treatment = "No chemical cure once infected — remove and destroy infected plants.\n" +
                "Apply copper-based bactericides early if disease pressure is high (helps slow spread).\n",
        prevention = "Avoid overhead watering..."
    )
    )
}

fun getDiseaseById(id: Int): DiseaseItem {
    return getDiseaseItems().first { it.id == id }
}