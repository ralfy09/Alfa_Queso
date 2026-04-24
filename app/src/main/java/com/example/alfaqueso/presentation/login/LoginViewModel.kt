package com.example.alfaqueso.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var usuario by mutableStateOf("")
    var clave by mutableStateOf("")
    var estaCargando by mutableStateOf(false)
    var errorLogin by mutableStateOf<String?>(null)

    fun onLoginClick(onLoginSuccess: () -> Unit) {
        errorLogin = null

        if (usuario.isBlank() || clave.isBlank()) {
            errorLogin = "Por favor, completa todos los campos"
            return
        }

        estaCargando = true

        viewModelScope.launch {
            delay(1000)

            if (usuario.trim() == "admin" && clave.trim() == "admin0909") {
                estaCargando = false
                onLoginSuccess()
            } else {
                estaCargando = false

                errorLogin = "Usuario o contraseña incorrectos"
            }
        }
    }
}