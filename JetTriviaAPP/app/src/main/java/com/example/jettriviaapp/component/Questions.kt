package com.example.jettriviaapp.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviaapp.model.QuestionItem
import com.example.jettriviaapp.screens.QuestionsViewModel
import com.example.jettriviaapp.util.AppColors
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Questions(viewmodel: QuestionsViewModel
) {
    val questions = viewmodel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }


    if (viewmodel.data.value.loading == true)
    {
        CircularProgressIndicator()
        Log.d("Loading", "Questions: ...Loading...")
    }
    else{
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception) {
            null
        }

        if(questions != null)
        {
            QuestionDisplay(question = question!!, questionIndex = questionIndex, viewmodel = viewmodel){
                questionIndex.value = questionIndex.value + 1
            }
        }
    }
}




@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewmodel: QuestionsViewModel,
    onNextClicked: (Int) -> Unit = {}
) {

    val choicesState = remember(question){
        question.choices.toMutableList()
    }

    val answerState = remember(question){
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question){
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {

            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer

        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            if(questionIndex.value >= 3) ShowProgress(score = questionIndex.value)

            QuestionTracker(counter = questionIndex.value, viewmodel.getTotalQuestionCount())
            DrawDottedLine(pathEffect)
            Column {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    color = AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp)

                choicesState.forEachIndexed { index, answerText ->
                    Row (
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults
                                .colors(
                                    selectedColor =
                                        if (correctAnswerState.value == true && index == answerState.value){
                                            Color.Green.copy(alpha = 0.2f)
                                        } else {
                                            Color.Red.copy(alpha = 0.2f)
                                        }
                                )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                color = if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green
                                } else if (correctAnswerState.value == false && index == answerState.value) {
                                    Color.Red
                                } else {
                                    AppColors.mOffWhite
                                },
                                fontSize = 17.sp)){

                                append(answerText)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))
                    }
                }
                Button(onClick = { onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue), content = {
                        Text(text = "Next",
                            modifier = Modifier.padding(4.dp),
                            color = AppColors.mOffWhite,
                            fontSize = 17.sp)

                    })
            }
        }
    }
}

@Composable
fun DrawDottedLine(
    pathEffect: PathEffect
) {
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect as androidx.compose.ui.graphics.PathEffect?
        )
    }
}







@Composable
fun QuestionTracker(
    counter: Int = 10,
    outOf: Int = 100
)
{
    Text(text = buildAnnotatedString {
        withStyle(
            style = ParagraphStyle(textIndent = TextIndent.None)
        ) {
            withStyle(
                style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Bold, fontSize = 27.sp)
            ) {
                append("Question $counter/")
                withStyle(
                    style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Light, fontSize = 14.sp)
                ){
                    append("$outOf")
                }
            }
        }
    },
        modifier = Modifier.padding(20.dp))
}

@Composable
fun ShowProgress(score: Int = 12) {

    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),
        Color(0xFFBE6BE5)))

    val progressFactor by remember(score) {
        mutableStateOf(score*0.005f)

    }
    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(width = 4.dp,
            brush = Brush.linearGradient(colors = listOf(AppColors.mLightPurple,
                AppColors.mLightPurple)),
            shape = RoundedCornerShape(34.dp))
        .clip(RoundedCornerShape(topStartPercent = 50,
            topEndPercent = 50,
            bottomEndPercent = 50,
            bottomStartPercent = 50))
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )) {
            Text(text = (score*10).toString(),
                modifier = Modifier.clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center)

//
        }


    }
}

