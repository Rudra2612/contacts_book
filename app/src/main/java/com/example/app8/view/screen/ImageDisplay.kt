package com.example.app8.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.app8.controller.HomeController
import com.example.app8.view.navigation.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ImageDisplay(navController: NavHostController, id: Int) {

    val context = LocalContext.current
    val homeController by lazy {
        HomeController(context)
    }
    val detail = homeController.getContactDetail(id)

    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            navController.navigate(ScreenRoute.ContactDetails.route + "/${id}") {
                                popUpTo(ScreenRoute.ImageDisplay.route) { inclusive = true }
                            }
                        })
            },
        )
    },
        modifier = Modifier
            .clickable{
                navController.navigate(ScreenRoute.ContactDetails.route + "/${id}") {
                    popUpTo(ScreenRoute.ImageDisplay.route) { inclusive = true }
                }
            }) { innerpadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            contentAlignment = Alignment.Center
        )
        {
            GlideImage(
                model = detail.image,
                contentDescription = null
            )
        }
    }
}