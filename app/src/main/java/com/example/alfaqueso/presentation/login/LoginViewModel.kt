package com.example.alfaqueso.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {

    var usuario by mutableStateOf("")
    var clave by mutableStateOf("")
    var estaCargando by mutableStateOf(false)
    var errorLogin by mutableStateOf<String?>(null)

    fun onLoginClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            errorLogin = null
            try {

                onSuccess()
            } catch (e: Exception) {
                errorLogin = "Usuario o clave incorrectos"
            } finally {
                estaCargando = false
            }
        }
    }
}