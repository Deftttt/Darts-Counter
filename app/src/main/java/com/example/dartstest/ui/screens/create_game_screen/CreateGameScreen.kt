package com.example.dartstest.ui.screens.create_game_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameScreen(
    createGameViewModel: CreateGameViewModel,
    onCreateGameClicked: (String) -> Unit,
) {

    val targetScore: Int by createGameViewModel.targetScore
    val legsToWin: Int by createGameViewModel.legsToWin

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TopAppBar(
            title = { Text("Create Game") },
            navigationIcon = {
                IconButton(onClick = { /* Handle navigation back */ }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                // Confirm Button
                IconButton(
                    onClick = {
                        createGameViewModel.createGame(onCreateGameClicked)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            //value = createGameViewModel.targetScore.value.toString(),
            value = targetScore.toString(),
            onValueChange = {
                //createGameViewModel.setTargetScore(it.toIntOrNull() ?: 0)
                createGameViewModel.targetScore.value = it.toIntOrNull() ?: 0
            },
            label = { Text("Target Score") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text Field for Legs to Win
        TextField(
            //value = createGameViewModel.legsToWin.value.toString(),
            value = legsToWin.toString(),
            onValueChange = {
                //createGameViewModel.setLegsToWin(it.toIntOrNull() ?: 0)
                createGameViewModel.legsToWin.value = it.toIntOrNull() ?: 0
            },
            label = { Text("Legs to Win") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


