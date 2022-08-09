package com.eeema.android.rickandmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eeema.android.rickandmortyapp.ui.splash.SplashScreen
import com.eeema.android.rickandmortyapp.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyTheme {
                SplashScreen()
            }
        }
    }
}
