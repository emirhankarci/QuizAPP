package com.example.jettriviaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettriviaapp.screens.QuestionsViewModel
import com.example.jettriviaapp.screens.TriviaHome
import com.example.jettriviaapp.ui.theme.JetTriviaAPPTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetTriviaAPPTheme {
                Surface(color = MaterialTheme.colors.background) {
                    TriviaHome()
                }
            }
        }
    }
}






