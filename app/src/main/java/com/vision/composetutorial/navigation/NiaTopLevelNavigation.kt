package com.vision.composetutorial.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.vision.composetutorial.foryou.ForYouDestination
import com.vision.composetutorial.R.string.for_you

class NiaTopLevelNavigation(private val navController: NavHostController) {

    fun navigateTo(destination: TopLevelDestination){
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = ForYouDestination.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = for_you
    ),
    TopLevelDestination(
        route = ForYouDestination.route.plus("test"),
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = for_you
    ),
)