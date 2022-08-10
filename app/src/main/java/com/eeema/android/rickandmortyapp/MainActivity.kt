@file:OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)

package com.eeema.android.rickandmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.eeema.android.rickandmortyapp.ui.characters.CharactersScreen
import com.eeema.android.rickandmortyapp.ui.constants.Constants
import com.eeema.android.rickandmortyapp.ui.detail.DetailScreen
import com.eeema.android.rickandmortyapp.ui.extensions.animatedComposable
import com.eeema.android.rickandmortyapp.ui.extensions.navigate
import com.eeema.android.rickandmortyapp.ui.model.Route
import com.eeema.android.rickandmortyapp.ui.splash.SplashScreen
import com.eeema.android.rickandmortyapp.ui.theme.RickAndMortyTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyTheme {
                SetupNavigationGraph()
            }
        }
    }
}

@Composable
fun SetupNavigationGraph() {
    val navigationController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navigationController,
        startDestination = Route.Splash.route
    ) {
        animatedComposable(Route.Splash.route) {
            SplashScreen(navigate = navigationController::navigate)
        }
        animatedComposable(Route.Characters.route) {
            CharactersScreen(navigate = navigationController::navigate)
        }
        animatedComposable(
            Route.Details.route.plus("/{${Constants.characterIdKey}}"),
            arguments = listOf(navArgument(Constants.characterIdKey) { type = NavType.IntType })
        ) {
            DetailScreen(id = it.arguments?.getInt(Constants.characterIdKey))
        }
    }
}
