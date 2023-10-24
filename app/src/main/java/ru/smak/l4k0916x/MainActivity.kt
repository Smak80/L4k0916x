package ru.smak.l4k0916x

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.smak.l4k0916x.ui.theme.L4k0916xTheme
import java.lang.Math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            L4k0916xTheme {
                val readyCards = arrayOf(
                    remember{ mutableStateOf(true) },
                    remember{ mutableStateOf(false) },
                    remember{ mutableStateOf(false) },
                    remember{ mutableStateOf(false) },
                    remember{ mutableStateOf(false) },
                )
                val op1 = Random.nextInt(1, 99)
                val op2 = Random.nextInt(1, 99)
                val operation = '+'
                val txt = stringResource(id = R.string.exercise, op1, operation, op2)
                Column(Modifier.fillMaxSize()) {
                    Exercise(txt, readyCards, 0, Modifier.fillMaxWidth())
                    Exercise(txt, readyCards, 1, Modifier.fillMaxWidth())
                    Exercise(txt, readyCards, 2, Modifier.fillMaxWidth())
                    Exercise(txt, readyCards, 3, Modifier.fillMaxWidth())
                    Exercise(txt, readyCards, 4, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise(
    text: String,
    readyCards: Array<MutableState<Boolean>>,
    cardIndex: Int,
    modifier: Modifier = Modifier,
) {
    var value by remember { mutableStateOf("") }
    var enabled by remember {mutableStateOf(true)}
    var rowColor by remember { mutableStateOf(Color.LightGray) }
    val (op1, op2) = text.split('+', '-', '*', '/', ' ', '=').filter { it.isNotBlank() }.map{ it.toIntOrNull() ?: 0 }
    Log.d("MA", "OP1=$op1; OP2=$op2")
    val operation = text.filter { it in arrayOf('+', '-', '*', '/') }[0]
    val result = when (operation){
        '+' -> op1 + op2
        '-' -> op1 - op2
        '*' -> op1 * op2
        else -> op1 / op2
    }
    ElevatedCard(
        modifier
            .padding(8.dp, 8.dp)
            .alpha(if (readyCards[cardIndex].value) 1f else 0f)) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(rowColor),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text,
                Modifier
                    .padding(16.dp, 32.dp)
                    .weight(3f), fontSize = 32.sp)
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = if (it.isEmpty() || abs(it.toIntOrNull() ?: 1000) < 1000) it else value
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ), singleLine = true,
                enabled = enabled
            )
            IconButton(onClick = {
                rowColor = if (checkAnswer(value, result)) Color(0.8f, 1f, 0.8f) else Color(1f, 0.8f, 0.8f)
                enabled = false
                if (cardIndex < readyCards.size - 1) {
                    readyCards[cardIndex + 1].value = true
                }
                Log.d("MA", readyCards.joinToString())
            }, enabled = enabled){
                Icon(painter = painterResource(
                    R.drawable.baseline_check_circle_outline_48
                ), contentDescription = "Answer button", tint = Color.Green)
            }
        }
    }
}

fun checkAnswer(value: String, result: Int): Boolean{
    val userV = value.toIntOrNull() ?: Int.MIN_VALUE
    Log.d("MA", "result=$result; value=$userV")
    return userV == result
}

@Preview
@Composable
fun ExercisePreview(){
    L4k0916xTheme {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            Exercise("15 + 7=", arrayOf(remember {
                mutableStateOf(true)
            }), 0)
        }
    }
}

