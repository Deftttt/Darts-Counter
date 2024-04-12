package com.example.dartstest.ui.screens.register_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dartstest.data.repository.AuthRepository
import com.example.dartstest.ui.screens.login_screen.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()


    fun registerUser(email: String, password: String, username: String) = viewModelScope.launch {
        repository.registerUser(email, password, username).collect{ result ->
            when(result){
                is com.example.dartstest.util.Resource.Success -> {
                    _signUpState.send(SignUpState(isSuccess = "Sign up success"))
                }

                is com.example.dartstest.util.Resource.Loading -> {
                    _signUpState.send(SignUpState(isLoading = true))
                }

                is com.example.dartstest.util.Resource.Error -> {
                    _signUpState.send(SignUpState(isError = result.message))
                }

            }
        }
    }

}