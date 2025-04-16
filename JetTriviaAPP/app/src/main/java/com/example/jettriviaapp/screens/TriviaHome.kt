package com.example.jettriviaapp.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettriviaapp.component.Questions

@Composable
fun TriviaHome(
    viewmodel: QuestionsViewModel = hiltViewModel()
) {
    Questions(viewmodel)
}