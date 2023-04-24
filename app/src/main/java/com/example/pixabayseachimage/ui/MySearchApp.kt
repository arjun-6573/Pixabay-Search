
package com.example.pixabayseachimage.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.pixabayseachimage.ui.theme.PixabaySeachImageTheme

@Composable
fun MySearchApp(
    widthSizeClass: WindowWidthSizeClass
) {
    PixabaySeachImageTheme {
        val navController = rememberNavController()
        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Medium
        MyAppNavGraph(isExpandedScreen, navController = navController)
    }
}
