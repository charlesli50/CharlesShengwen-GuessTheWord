package com.example.charlesshengwen_guesstheword

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.charlesshengwen_guesstheword.ui.theme.CharlesShengwenGuessTheWordTheme
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharlesShengwenGuessTheWordTheme {
                var gameWord by remember { mutableStateOf("PIZZA") } // Example word for testing
                var selectedLetters = remember { mutableStateOf(List(26) { false }) }
                var wrongGuesses = remember { mutableStateOf(0) }
                var hintUsed = remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            selectedLetters.value = List(26) { false }
                            wrongGuesses.value = 0
                            hintUsed.value = 0 // Reset hint counter
                            gameWord = "BURGER" // New game word
                        }) {
                            Text("New Game")
                        }
                    }
                ) { innerPadding ->
                    GuessingGame(
                        modifier = Modifier.padding(innerPadding),
                        gameWord = gameWord,
                        selectedLetters = selectedLetters,
                        wrongGuesses = wrongGuesses,
                        hintUsed = hintUsed
                    )
                }
            }
        }
    }
}

@Composable
fun GuessingGame(
    modifier: Modifier = Modifier,
    gameWord: String,
    selectedLetters: MutableState<List<Boolean>>,
    wrongGuesses: MutableState<Int>,
    hintUsed: MutableState<Int>
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {

            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                MainGame(
                    gameWord = gameWord,
                    selectedLetters = selectedLetters,
                    wrongGuesses = wrongGuesses.value,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                )
                ChooseTheLetter(
                    selectedLetters = selectedLetters,
                    gameWord = gameWord,
                    wrongGuesses = wrongGuesses
                )
            }

    } else {
        // 横屏布局保持不变
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ChooseTheLetter(selectedLetters, gameWord, wrongGuesses)
                HintButton(gameWord, selectedLetters, hintUsed, wrongGuesses, false)
            }

            MainGame(
                gameWord = gameWord,
                selectedLetters = selectedLetters,
                wrongGuesses = wrongGuesses.value,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            )
        }
    }
}



@Composable
fun MainGame(
    gameWord: String,
    selectedLetters: MutableState<List<Boolean>>,
    wrongGuesses: Int,
    modifier: Modifier = Modifier
): Pair<Boolean, Boolean> {
    val correctLetters = gameWord.map { if (selectedLetters.value[it - 'A']) it else '_' }.joinToString(" ")
    val isGameOver = wrongGuesses >= 6
    val isGameWon = correctLetters.replace(" ", "") == gameWord

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = if (isGameOver) "Game Over" else if (isGameWon) "You Won!" else "Guess the word!", fontSize = 24.sp)
        Text(text = correctLetters, fontSize = 36.sp)

        // Draw Hangman
        HangmanDrawing(wrongGuesses)
    }
    return Pair(isGameOver, isGameWon)
}

@Composable
fun HangmanDrawing(wrongGuesses: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (wrongGuesses > 0) Text("Head")
        if (wrongGuesses > 1) Text("Body")
        if (wrongGuesses > 2) Text("Left Arm")
        if (wrongGuesses > 3) Text("Right Arm")
        if (wrongGuesses > 4) Text("Left Leg")
        if (wrongGuesses > 5) Text("Right Leg")
    }
}

@Composable
fun ChooseTheLetter(
    selectedLetters: MutableState<List<Boolean>>,
    gameWord: String,
    wrongGuesses: MutableState<Int>
) {
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
                        val letter = ('A' + index).toString()
                        Button(
                            onClick = {
                                val isCorrect = gameWord.contains(letter)
                                if (!isCorrect) {
                                    wrongGuesses.value++
                                }
                                selectedLetters.value = selectedLetters.value.toMutableList().apply {
                                    this[index] = true
                                }
                            },
                            enabled = !selectedLetters.value[index],
                            modifier = Modifier
                                .weight(1f)
                                .padding(1.dp)
                                .height(30.dp)
                        ) {
                            Text(
                                letter,
                                fontSize = 16.sp,
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
fun HintButton(
    gameWord: String,
    selectedLetters: MutableState<List<Boolean>>,
    hintUsed: MutableState<Int>,
    wrongGuesses: MutableState<Int>,
    isDisabled: Boolean // Add this parameter
) {
    val context = LocalContext.current // Get the context in a composable-friendly way

    Button(
        onClick = {
            when (hintUsed.value) {
                0 -> Toast.makeText(context, "Hint: Food", Toast.LENGTH_SHORT).show()
                1 -> {
                    // Disable half of the letters (not in word)
                    val notInWordLetters = (0..25).filter { !gameWord.contains(('A' + it)) }
                    val halfLetters = notInWordLetters.shuffled().take(notInWordLetters.size / 2)
                    selectedLetters.value = selectedLetters.value.toMutableList().apply {
                        halfLetters.forEach { this[it] = true }
                    }
                    wrongGuesses.value++
                }
                2 -> {
                    // Show all vowels
                    val vowels = listOf('A', 'E', 'I', 'O', 'U')
                    selectedLetters.value = selectedLetters.value.toMutableList().apply {
                        vowels.forEach { this[it - 'A'] = true }
                    }
                    wrongGuesses.value++
                }
            }
            hintUsed.value++
        },
        enabled = !isDisabled // Disable if game is won or over
    ) {
        Text(text = "Hint")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CharlesShengwenGuessTheWordTheme {
        GuessingGame(
            gameWord = "PIZZA",
            selectedLetters = remember { mutableStateOf(List(26) { false }) },
            wrongGuesses = remember { mutableStateOf(0) },
            hintUsed = remember { mutableStateOf(0) }
        )
    }
}
