package com.vision.composetutorial.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vision.composetutorial.foryou.ForYouDestination
import com.vision.composetutorial.foryou.forYouGraph

@Composable
fun NiaNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ForYouDestination.route
){
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ){
        forYouGraph(windowSizeClass)
        forYouGraph(windowSizeClass)
    }
}