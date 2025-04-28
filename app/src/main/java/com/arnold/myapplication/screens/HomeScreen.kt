package com.arnold.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onCaptureImage: () -> Unit = {}
) {
    // Phone UI Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)


            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()


                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)


                    ) {
                        // Welcome Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "welcome to ShambaTech",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                )

                                // Features List
                                FeaturesCard()

                                // Capture Button
                                Button(
                                    onClick = onCaptureImage,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green button
                                ) {
                                    Text("Capture Image")
                                }
                            }
                        }
                    }
                }
            }
        }


@Composable
private fun FeaturesCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.White, shape = RoundedCornerShape(8.dp))
            .padding(bottom = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            FeatureItem(
                text = "Turn your Android phone into a mobile crop doctor"
            )
            Spacer(modifier = Modifier.height(12.dp))
            FeatureItem(
                text = "With just one photo, ShambaTech diagnoses infected crops and offers treatments for any disease"
            )
            Spacer(modifier = Modifier.height(12.dp))
            FeatureItem(
                text = "Benefit from agricultural experts' knowledge or help fellow farmers with your knowledge"
            )
        }
    }
}

@Composable
private fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "✓",
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}




