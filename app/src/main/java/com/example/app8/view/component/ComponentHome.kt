@file:Suppress("MISSING_DEPENDENCY_CLASS_IN_EXPRESSION_TYPE")

package com.example.app8.view.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.app8.R
import com.example.app8.controller.HomeController
import com.example.app8.model.ContactModel
import com.example.app8.ui.theme.callColor
import com.example.app8.ui.theme.theme
import com.example.app8.ui.theme.themeColor
import com.example.app8.view.navigation.ScreenRoute

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageOrText(
    user: Int,
    image: String,
    firstname: String,
    paddingValues: Int,
    fontSize: Int,
    circleSize: Int,
    circleColor: String,
    onclick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .size(circleSize.dp)
            .background(color = hexStringToColor(circleColor), shape = CircleShape)
    ) {
        when {
            image.isNotEmpty() -> {
                GlideImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable { onclick?.invoke() })
            }

            firstname.first().isLetter() == true -> {
                Text(
                    text = firstname.first().uppercase(),
                    fontSize = fontSize.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues.dp),
                )
            }

            else -> {
                Image(
                    painter = painterResource(user),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ContactList(
    contactList: List<ContactModel>,
    navController: NavHostController,
    innerpadding: PaddingValues? = null,
    isSearch: Boolean,
    context: Context,
    onSelectionChanged: (ContactModel) -> Unit
) {
    val listState = rememberLazyListState()
    val isSelectionMode = contactList.any { it.selected.value == "true" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding ?: PaddingValues(0.dp)),
        state = listState
    ) {
        if (!isSearch) {
            val favoriteContact = contactList.filter { it.favorite == "true" }

            items(favoriteContact.size) {
                val contact = favoriteContact[it]

                if (it == 0) {
                    Row(
                        modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.fill_star),
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Favorite",
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                ContactRow(context, contact, navController,isSelectionMode,onSelectionChanged)
            }
        }

        items(contactList.size) {
            val contact = contactList[it]

            if (innerpadding != null) {
                if (contact.label.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = contact.label.uppercase(),
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 5.dp)
                            .fillMaxWidth()
                    )
                }
            }

            ContactRow(context, contact, navController,isSelectionMode,onSelectionChanged)
        }
    }

    LaunchedEffect(Unit) {
        listState.animateScrollToItem(0)
    }
}

@Composable
fun ContactRow(
    context: Context,
    contact: ContactModel,
    navController: NavHostController,
    isSelectionMode: Boolean,
    onSelectionChanged: (ContactModel) -> Unit
) {
    val homeController by lazy { HomeController(context) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    var showDialogPhone by remember { mutableStateOf(false) }
    var dPhone by remember { mutableStateOf(contact.dPhone) }
    val phone = contact.phone.split("_0_")

    Row(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        contact.selected.value = if (contact.selected.value == "true") "false" else "true"
                        onSelectionChanged(contact)
                    } else {
                        navController.navigate(ScreenRoute.ContactDetails.route + "/${contact.id}")
                    }
                },
                onLongClick = {
                    contact.selected.value = "true"
                    onSelectionChanged(contact)
                    homeController.contactSelected(contact, contact.id)
                }
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (contact.selected.value == "false") {
                        offsetX += dragAmount * 4f

                    }
                    if (offsetX >= 400f) {
                        offsetX = 0f
                        if (contact.dPhone.isNotEmpty()) {
                            call(context = context, dPhone)
                        } else if (contact.phone.isNotEmpty()) {
                            showDialogPhone = true
                        }
                    }
                }
            }
            .padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
        ) {
            if (contact.selected.value == "false") {
                if (offsetX != 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(offsetX.dp)
                            .background(color = callColor)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = if(contact.selected.value == "true") 5.dp else 10.dp,
                        top = if(contact.selected.value == "true") 0.dp else 8.dp,
                        end = if(contact.selected.value == "true") 10.dp else 0.dp
                    )
                    .background(
                        color = if (contact.selected.value == "true") themeColor else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                if (contact.selected.value == "true") {
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .padding(start = 5.dp, top = 5.dp)
                            .background(color = theme, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp)
                        )
                    }
                } else {
                    ImageOrText(
                        R.drawable.user,
                        contact.image,
                        contact.firstname,
                        paddingValues = 10,
                        fontSize = 35,
                        circleSize = 60,
                        circleColor = contact.color,
                    )
                }

                Text(
                    text = "${contact.firstname} ${contact.surname}",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(start = 20.dp, top = if(contact.selected.value == "true") 20.dp else 15.dp)
                        .fillMaxWidth()
                )
            }
        }

        if (showDialogPhone) {
            DefaultSelection(
                label = "phone number",
                option = phone,
                justOnce = { dPhone = it
                    call(context, dPhone)
                    dPhone = ""
                },
                always = { dPhone = it
                    contact.dPhone = dPhone
                    call(context, dPhone)
                    HomeController(context).updateDefault(contact, contact.id)
                },
                onDismiss = {
                    showDialogPhone = false
                    offsetX = 0f
                })
        }
    }
}

fun hexStringToColor(hex: String): Color {
    val cleanedHex = hex.removePrefix("#")
    val alpha = cleanedHex.substring(0, 2).toInt(16)
    val red = cleanedHex.substring(2, 4).toInt(16)
    val green = cleanedHex.substring(4, 6).toInt(16)
    val blue = cleanedHex.substring(6, 8).toInt(16)
    return Color(red, green, blue, alpha)
}