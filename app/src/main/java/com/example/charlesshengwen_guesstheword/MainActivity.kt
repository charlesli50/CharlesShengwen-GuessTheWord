package com.example.charlesshengwen_guesstheword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.charlesshengwen_guesstheword.ui.theme.CharlesShengwenGuessTheWordTheme

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharlesShengwenGuessTheWordTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GuessingGame(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GuessingGame(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT


    //list of selected characters, character 'A' + index is selected if that index is true
    val selectedLetters = remember { mutableStateOf(List(26) { false }) }


    // panels
    if (isPortrait) {
        Column {
            MainGame()
            ChooseTheLetter(selectedLetters)
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth()){
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChooseTheLetter(selectedLetters)
                HintButton()
            }

            MainGame(modifier = Modifier
                .weight(1f)
                .padding(16.dp))
        }
    }
}

@Composable
fun MainGame(modifier: Modifier = Modifier) {
    Text(
        text = "Main Game!",
        modifier = modifier
    )
}

@Composable
fun ChooseTheLetter(selectedLetters:MutableState<List<Boolean>>, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Create rows of buttons
        for (row in 0..4) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0..6) {
                    val index = row * 7 + col
                    if (index < 26) {
                        val letter = ('A' + index).toString()  // Get the corresponding letter
                        Button(
                            onClick = {
                                selectedLetters.value = selectedLetters.value.toMutableList().apply { this[index] = true }
                            },
                            enabled = !selectedLetters.value[index], // disable button if already selected
                            modifier = Modifier
                                    .weight(5f)
                                .padding(1.dp)
                                .height(30.dp)
                                .width(20.dp),
                            contentPadding = PaddingValues(0.dp) //makes the text not get clipped by padding
                        ) {
                            Text(
                                letter,
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxSize(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HintButton(modifier: Modifier = Modifier) {
    Text(
        text = "Hint Button!",
        modifier = modifier
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CharlesShengwenGuessTheWordTheme {
        GuessingGame()
    }
}