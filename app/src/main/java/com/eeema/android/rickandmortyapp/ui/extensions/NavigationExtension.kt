package com.eeema.android.rickandmortyapp.ui.extensions

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.eeema.android.rickandmortyapp.ui.model.Route

fun NavHostController.navigate(nextRoute: Route) {
    navigate(nextRoute.route) {
        popUpTo(currentDestination?.route, nextRoute.removePreviousScreens)
    }
}

private fun NavOptionsBuilder.popUpTo(route: String?, removeInclusive: Boolean = false) {
    if (!route.isNullOrBlank()) {
        popUpTo(route) {
            inclusive = removeInclusive
        }
    }
}
