package com.example.wordme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.wordme.ui.WordLearningScreen
import com.example.wordme.ui.theme.WordMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordMeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WordLearningScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}