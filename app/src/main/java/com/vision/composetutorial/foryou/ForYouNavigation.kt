package com.vision.composetutorial.foryou

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vision.composetutorial.navigation.NiaNavigationDestination

object ForYouDestination: NiaNavigationDestination {
    override val route = "for_you_route"
    override val destination = "for_you_destination"
}

fun NavGraphBuilder.forYouGraph(
    windowSizeClass: WindowSizeClass,
){
    composable(route = ForYouDestination.route){
        ForYouRoute(windowSizeClass = windowSizeClass)
    }
}