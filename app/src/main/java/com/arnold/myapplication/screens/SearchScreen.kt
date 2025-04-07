package com.arnold.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnold.myapplication.R

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2F1)) // Light mint green background
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    color = Color(0xFF2E7D32), // Deep green
                    shape = RoundedCornerShape(bottomStart = 32.dp)
                )
        ) {
            // White foreground card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(16.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side text
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Other",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "pest & diseases",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    // Right side image
                    Image(
                        painter = painterResource(id = R.drawable.ic_sprout), // Replace with your image
                        contentDescription = "Plant sprout",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Disease List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getDiseaseItems()) { disease ->
                DiseaseCard(disease)
            }
        }
    }
}

@Composable
fun DiseaseCard(disease: DiseaseItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side image
            Image(
                painter = painterResource(id = disease.imageRes),
                contentDescription = disease.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Right side text
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = disease.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = disease.affectedPlants,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Data class for disease items
data class DiseaseItem(
    val name: String,
    val affectedPlants: String,
    val imageRes: Int
)

// Sample data
fun getDiseaseItems(): List<DiseaseItem> {
    return listOf(
        DiseaseItem(
            name = "Cinnamon spot Leaf",
            affectedPlants = "potato, sukuma & spinach",
            imageRes = R.drawable.leaf1 // Replace with your image
        ),
        DiseaseItem(
            name = "Early Blight",
            affectedPlants = "tomato, potato",
            imageRes = R.drawable.leaf1 // Replace with your image
        ),
        DiseaseItem(
            name = "Late Blight",
            affectedPlants = "tomato, potato",
            imageRes = R.drawable.leaf1 // Replace with your image
        ),
        DiseaseItem(
            name = "Powdery Mildew",
            affectedPlants = "cucumber, squash",
            imageRes = R.drawable.leaf1 // Replace with your image
        ),
        DiseaseItem(
            name = "Powdery Mildew",
            affectedPlants = "cucumber, squash",
            imageRes = R.drawable.leaf1 // Replace with your image
        ),
        DiseaseItem(
            name = "Powdery Mildew",
            affectedPlants = "cucumber, squash",
            imageRes = R.drawable.leaf1 // Replace with your image
        )

    )
}