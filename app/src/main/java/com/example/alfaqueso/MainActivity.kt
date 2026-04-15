package com.example.alfaqueso


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.alfaqueso.presentation.AlfaQuesoNavGraph
import com.example.alfaqueso.ui.theme.AlfaQuesoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlfaQuesoTheme {
                // El NavGraph decide si mostrar Login o Dashboard
                AlfaQuesoNavGraph()
            }
        }
    }
}