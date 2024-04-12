package com.example.dartstest.navigation

sealed class Screens(val route: String) {
    object SignInScreen : Screens(route = "SignIn_Screen")
    object SignUpScreen : Screens(route = "SignUp_Screen")
    object CreateGameScreen : Screens(route = "CreateGame_Screen")
    data class GameScreen(val gameId: String) : Screens(route = "Game_Screen/$gameId")
}