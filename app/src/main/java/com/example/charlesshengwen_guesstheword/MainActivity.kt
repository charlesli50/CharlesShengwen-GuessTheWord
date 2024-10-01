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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

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

    if (isPortrait) {
        Column {
            MainGame()
            ChooseTheLetter()
        }
    } else {
        Row {
            Column {
                ChooseTheLetter()
                HintButton()
            }

            MainGame()
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
fun ChooseTheLetter(modifier: Modifier = Modifier) {
    Text(
        text = "Choose the letter!",
        modifier = modifier
    )
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