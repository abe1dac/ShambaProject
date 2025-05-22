package com.arnold.myapplication.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arnold.myapplication.ml.Classifier

@Composable
fun ResultScreen(
    results: List<Classifier.Result>,
    modifier: Modifier = Modifier
) {
    Log.e("ResultScreen", "results: ${results.size}")

    if (results.isEmpty()) {
        Log.e("ResultScreen", "results is empty")
    } else {
        Log.e("ResultScreen", "results is not empty")
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Classification Results",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(16.dp))
        if (results.isEmpty()) {
            Text(text = "No results found.")
        } else {
            LazyColumn {
                items(results) { result ->
                    ResultItem(result = result)
                }
            }
        }
    }
}

@Composable
fun ResultItem(result: Classifier.Result) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Label: ${result.title}", fontWeight = FontWeight.Bold)
        Text(text = "Confidence: ${String.format("%.2f", result.confidence * 100)}%")
    }
}