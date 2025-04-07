package com.arnold.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onCaptureImage: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // Light green background color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome title
            Text(
                text = "Welcome ",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32) // Dark green color
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "To ShambaTech App ",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32) // Dark green color
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Which crop disease may I diagnose for you today?",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF1B5E20) // Slightly darker green color
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Capture Image Button
            Button(
                onClick = onCaptureImage,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green button
            ) {
                Text("Capture Image")
            }
        }
    }
}