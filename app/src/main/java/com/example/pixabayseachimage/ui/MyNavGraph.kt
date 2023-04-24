package com.example.pixabayseachimage.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pixabayseachimage.ui.imageDetails.ImageDetailsScreen
import com.example.pixabayseachimage.ui.searchImages.SearchImageScreen

@Composable
fun MyAppNavGraph(
    isLandscapeMode: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MyDestinations.SEARCH_IMAGE_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MyDestinations.SEARCH_IMAGE_ROUTE) {
            SearchImageScreen(
                isLandscapeMode = isLandscapeMode,
                navController = navController
            )
        }
        composable(MyDestinations.IMAGE_DETAIL_ROUTE) {
            ImageDetailsScreen(
                isLandscapeMode = isLandscapeMode,
                navController = navController
            )
        }
    }
}

object MyDestinations {
    const val SEARCH_IMAGE_ROUTE = "searchImage"
    const val IMAGE_DETAIL_ROUTE = "imageDetails"
}