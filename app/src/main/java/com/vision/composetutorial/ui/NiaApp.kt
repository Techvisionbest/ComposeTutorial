package com.vision.composetutorial.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vision.composetutorial.component.NiaBackground
import com.vision.composetutorial.navigation.NiaNavHost
import com.vision.composetutorial.navigation.NiaTopLevelNavigation
import com.vision.composetutorial.navigation.TOP_LEVEL_DESTINATIONS
import com.vision.composetutorial.navigation.TopLevelDestination
import com.vision.composetutorial.ui.theme.NiaTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NiaApp(windowSizeClass: WindowSizeClass){
    NiaTheme {
        val navController = rememberNavController()
        val niaTopLevelNavigation = remember(navController) {
            NiaTopLevelNavigation(navController)
        }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NiaBackground {
            Scaffold(
                modifier = Modifier,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                bottomBar = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact){
                        NiaBottomBar(
                            onNavigationToTopLevelDestination = niaTopLevelNavigation::navigateTo,
                            currentDestination = currentDestination,
                        )
                    }
                }

            ) { padding ->
                Row(
                    Modifier.fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            )
                        )
                ) {
                    if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact){
                        NiaNavRail(
                            onNavigateToTopLevelDestination = niaTopLevelNavigation::navigateTo,
                            currentDestination = currentDestination,
                            modifier = Modifier.safeDrawingPadding()
                        )
                    }

                    NiaNavHost(
                        windowSizeClass = windowSizeClass,
                        navController = navController,
                        modifier = Modifier
                            .padding(padding)
                            .consumedWindowInsets(padding)
                    )
                }
            }
        }

    }
}

@Composable
private fun NiaNavRail(
    onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToTopLevelDestination(destination) },
                icon = {
                    Icon(
                        if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) }
            )
        }
    }
}

@Composable
private fun NiaBottomBar(
    onNavigationToTopLevelDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
){
    Surface(color = MaterialTheme.colorScheme.surface) {
        NavigationBar(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            ),
            tonalElevation = 0.dp,
        ) {
            TOP_LEVEL_DESTINATIONS.forEach { destination ->
                val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick =  { onNavigationToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (selected){
                                destination.selectedIcon
                            }else{
                                destination.unselectedIcon
                            },
                            contentDescription = null,
                        )
                    },
                    label = { Text(text = stringResource(id = destination.iconTextId))}
                )
            }
        }
    }

}