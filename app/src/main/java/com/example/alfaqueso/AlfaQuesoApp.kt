package com.example.alfaqueso // Asegúrate de que coincida con tu paquete

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Esta etiqueta es la que enciende la inyección de dependencias en toda la app
@HiltAndroidApp
class AlfaQuesoApp : Application()