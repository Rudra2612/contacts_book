package com.example.app8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.app8.ui.theme.App8Theme
import com.example.app8.view.navigation.NavigationStack

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App8Theme {
                NavigationStack()
            }
        }
    }
}