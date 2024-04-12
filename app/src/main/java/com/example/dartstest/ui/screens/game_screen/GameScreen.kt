package com.example.dartstest.ui.screens.game_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dartstest.data.model.ScoringType
import com.example.dartstest.data.model.Throw
import com.example.dartstest.data.model.Type

@Composable
fun GameScreen(
    gameId: String,
    gameViewModel: GameViewModel
) {

    LaunchedEffect(gameId) {
        gameViewModel.observeGame(gameId)
    }

    val game by gameViewModel.game.collectAsState()

    val scoringType: Type by gameViewModel.scoringType

    Log.d("GAME_STATUSSSSS", game.toString())
    // Buduj interfejs użytkownika
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Hello, $gameId!", fontSize = 20.sp)
        Text(
            text = "Points to achieve: ${game?.remainingPoints}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )


        NumberPad(
            onNumberClick = { singleThrow ->
                gameViewModel.processThrow(singleThrow)
            },
            onUndoClick = {},
            onScoringTypeChanged = {
                gameViewModel.scoringType.value = it
            },
            scoringType = scoringType
        )


    }
}

@Composable
fun NumberPad(
    onNumberClick: (Throw) -> Unit,
    onUndoClick: () -> Unit,
    scoringType: Type,
    onScoringTypeChanged: (Type) -> Unit
) {
    // Tworzymy dwuwymiarową listę przycisków od 1 do 20
    val buttons = (1..20).chunked(5)

    // Budujemy interfejs użytkownika
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScoreRow(score1 = "1st", score2 = "2nd", score3 = "3rd")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoringTypeButton(Type.S, scoringType, onScoringTypeChanged)
            ScoringTypeButton(Type.D, scoringType, onScoringTypeChanged)
            ScoringTypeButton(Type.T, scoringType, onScoringTypeChanged)
        }

        for (row in buttons) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (number in row) {
                    NumberButton(number, onNumberClick, scoringType)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            NumberButton(number = 25, onNumberClick, scoringType)
            NumberButton(number = 0, onNumberClick, scoringType)

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton("Undo") { /* gameViewModel.onUndoButtonClicked() */ }
            ActionButton("Confirm") { /* gameViewModel.onConfirmButtonClicked() */ }
        }
    }
}


@Composable
fun NumberButton(number: Int, onNumberClick: (Throw) -> Unit, scoringType: Type) {
    val singleThrow =
        if (number == 25 && scoringType == Type.T)
            Throw(number, Type.D)
        else
            Throw(number, scoringType)

    TextButton(
        onClick = {
            onNumberClick(singleThrow)
        },
        modifier = Modifier
            .padding(4.dp)
            .width(60.dp)
    ) {
        Text(text = number.toString() + scoringType, fontSize = 16.sp)
    }
}


@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .width(120.dp)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun ScoreRow(score1: String, score2: String, score3: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ScoreField(score1)
        ScoreField(score2)
        ScoreField(score3)
    }
}

@Composable
fun ScoreField(score: String) {
    Text(
        text = score,
        modifier = Modifier
            .padding(8.dp),
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun ScoringTypeButton(
    type: Type,
    currentType: Type,
    onScoringTypeChanged: (Type) -> Unit
) {
    Button(
        onClick = { onScoringTypeChanged(type) },
        modifier = Modifier
            .padding(4.dp)
            .width(80.dp),
        enabled = type != currentType
    ) {
        Text(
            text = type.full,
            fontSize = 16.sp
        )
    }
}


@Preview
@Composable
fun NumberPadPreview() {
    NumberPad(onNumberClick = {}, onUndoClick = {}, scoringType = Type.S, onScoringTypeChanged = {})
}
