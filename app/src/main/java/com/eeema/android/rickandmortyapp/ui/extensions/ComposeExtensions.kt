@file:OptIn(ExperimentalAnimationApi::class)

package com.eeema.android.rickandmortyapp.ui.extensions

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

fun NavGraphBuilder.animatedComposable(
    route: String,
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) =
        { slideIntoContainer(SlideDirection.Left, animationSpec = tween(1000)) },
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) =
        { slideOutOfContainer(SlideDirection.Left, animationSpec = tween(1000)) },
    popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition) =
        { slideIntoContainer(SlideDirection.Right, animationSpec = tween(1000)) },
    popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition) =
        { slideOutOfContainer(SlideDirection.Right, animationSpec = tween(1000)) },
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition,
        content = content
    )
}
