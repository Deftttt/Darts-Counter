package com.example.dartstest.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dartstest.ui.screens.create_game_screen.CreateGameScreen
import com.example.dartstest.ui.screens.game_screen.GameScreen
import com.example.dartstest.ui.screens.login_screen.SignInScreen
import com.example.dartstest.ui.screens.login_screen.SignInViewModel
import com.example.dartstest.ui.screens.register_screen.SignUpScreen
import com.example.dartstest.ui.screens.register_screen.SignUpViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SignUpScreen.route
    ) {
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(
                viewModel = hiltViewModel(),
                onSignUpInstead = {
                    navController.navigate(Screens.SignUpScreen.route)
                },
                onSignInSubmitted = {
                    navController.navigate(Screens.CreateGameScreen.route)
                }
            )
        }

        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(
                viewModel = hiltViewModel(),
                onSignInInstead = {
                    navController.navigate(Screens.SignInScreen.route)
                },
                onSignUpSubmitted = {

                }
                )
        }

        composable(route = Screens.CreateGameScreen.route) {
            CreateGameScreen(
                createGameViewModel = hiltViewModel(),
                onCreateGameClicked = {
                    navController.navigate(Screens.GameScreen(it).route)
                }
            )
        }


        composable(route = Screens.GameScreen("{gameId}").route) {
            val gameId = it.arguments?.getString("gameId")
            if (gameId != null) {
                GameScreen(
                    gameId = gameId,
                    gameViewModel = hiltViewModel()
                )
            }
        }


    }

}