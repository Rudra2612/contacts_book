package com.example.app8.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app8.view.screen.AddOrEditContact
import com.example.app8.view.screen.ContactDetails
import com.example.app8.view.screen.Home
import com.example.app8.view.screen.ImageDisplay
import com.example.app8.view.screen.Splash

@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    NavHost(navController, ScreenRoute.Splash.route) {
        composable(ScreenRoute.Splash.route) {
            Splash(navController)
        }
        composable(ScreenRoute.Home.route) {
            Home(navController)
        }
        composable(ScreenRoute.AddOrEditContact.route + "/{id}") { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id")
                AddOrEditContact(navController, id?.toIntOrNull())
        }
        composable(ScreenRoute.ContactDetails.route + "/{id}") { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id") ?: "0"
            ContactDetails(navController, id.toInt())
        }
        composable(ScreenRoute.ImageDisplay.route + "/{id}") { navBackStackEnter ->
            val id = navBackStackEnter.arguments?.getString("id") ?: ""
            ImageDisplay(navController, id.toInt())
        }
    }

}

sealed class ScreenRoute(val route: String) {
    data object Splash : ScreenRoute("splash")
    data object Home : ScreenRoute("home")
    data object AddOrEditContact : ScreenRoute("addOrEditContact")
    data object ContactDetails : ScreenRoute("contactDetails")
    data object ImageDisplay : ScreenRoute("ImageDisplay")
}
