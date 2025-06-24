package com.example.app8.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.app8.R
import com.example.app8.view.navigation.ScreenRoute
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {

    LaunchedEffect(true) {
        delay(1000)

        navController.navigate(ScreenRoute.Home.route) {
            popUpTo(ScreenRoute.Splash.route) {
                inclusive = true
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(R.drawable.logo1), contentDescription = null)
    }
}