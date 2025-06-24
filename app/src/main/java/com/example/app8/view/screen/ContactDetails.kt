@file:Suppress("DEPRECATION")

package com.example.app8.view.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.example.app8.R
import com.example.app8.controller.HomeController
import com.example.app8.ui.theme.boxColor
import com.example.app8.view.component.AddPhoneOrEmail
import com.example.app8.view.component.ImageBox
import com.example.app8.view.component.ImageOrText
import com.example.app8.view.component.InfoRow
import com.example.app8.view.component.ShowMenuOnLongPress
import com.example.app8.view.component.call
import com.example.app8.view.component.DefaultSelection
import com.example.app8.view.component.direction
import com.example.app8.view.component.email
import com.example.app8.view.component.textMessage
import com.example.app8.view.component.videoCall
import com.example.app8.view.navigation.ScreenRoute

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UseKtx", "ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContactDetails(navController: NavHostController, id: Int) {

    val context = LocalContext.current

    val homeController by lazy { HomeController(context) }
    val detail = HomeController(context).getContactDetail(id)

    var favorite by remember { mutableStateOf(detail.favorite) }
    var showDropMenu by remember { mutableStateOf(false) }

    val email = detail.email.split("_0_")
    val lEmail = detail.lEmail.split("_0_")

    val address = detail.address.split("_0_")
    val lAddress = detail.lAddress.split("_0_")

    val phone = detail.phone.split("_0_")
    val lPhone = detail.lPhone.split("_0_")

    var showDialogPhone by remember { mutableStateOf(false) }
    var showDialogEmail by remember { mutableStateOf(false) }
    var showDialogAddress by remember { mutableStateOf(false) }
    var numberUse by remember { mutableStateOf("") }

    var pressFieldText by remember { mutableStateOf("") }

    var dPhone by remember { mutableStateOf(detail.dPhone) }
    var dEmail by remember { mutableStateOf(detail.dEmail) }
    var dAddress by remember { mutableStateOf(detail.dAddress) }

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
                            navController.navigate(ScreenRoute.Home.route) {
                                popUpTo(ScreenRoute.ContactDetails.route) {
                                    inclusive = false
                                }
                            }
                        })
            },
            actions = {
                Icon(
                    painter = painterResource(R.drawable.pencil),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable {
                            navController.navigate(ScreenRoute.AddOrEditContact.route + "/${detail.id}")
                        }
                )
                Icon(
                    painter = if (favorite == "true") painterResource(R.drawable.fill_star)
                    else painterResource(R.drawable.star),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .clickable {
                            favorite = if (favorite == "true") {
                                "false"
                            } else {
                                "true"
                            }
                            detail.favorite = favorite
                            homeController.updateDefault(detail, id)
                        })
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            showDropMenu = true
                        }
                )
                DropdownMenu(
                    expanded = showDropMenu,
                    onDismissRequest = { showDropMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "delete") },
                        onClick = {
                            showDropMenu = false
                            homeController.deleteContact(id)
                            navController.navigate(ScreenRoute.Home.route) {
                                popUpTo(ScreenRoute.ContactDetails.route) {
                                    inclusive = true
                                }
                            }
                        })
                    DropdownMenuItem(
                        text = { Text(text = "share contact") },
                        onClick = {
                            showDropMenu = false
                        })
                }
            },
        )
    }) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            ImageOrText(
                R.drawable.user,
                detail.image,
                detail.firstname,
                paddingValues = 30,
                fontSize = 110,
                circleSize = 190,
                circleColor = detail.color
            ) {
                navController.navigate(ScreenRoute.ImageDisplay.route + "/${id}")
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(detail.firstname + " " + detail.surname, fontSize = 40.sp)

            if (detail.company.isNotEmpty()) {
                Spacer(modifier = Modifier.height(15.dp))

                Text(detail.company, fontSize = 17.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageBox(R.drawable.phone, text = "Call") {
                    if (detail.dPhone == "") {
                        if (phone.size > 1) {
                            showDialogPhone = true
                            numberUse = "Call"
                        } else {
                            call(context, phone[0])
                        }
                    } else {
                        call(context, dPhone)
                    }
                }

                ImageBox(R.drawable.message, text = "Message") {
                    if (detail.dPhone == "") {
                        if (phone.size > 1) {
                            showDialogPhone = true
                            numberUse = "Message"
                        } else {
                            textMessage(context, phone[0])
                        }
                    } else {
                        textMessage(context, dPhone)
                    }
                }

                ImageBox(R.drawable.video, text = "Video") {
                    if (detail.dPhone == "") {
                        if (phone.size > 1) {
                            showDialogPhone = true
                            numberUse = "Video"
                        } else {
                            videoCall(context, phone[0])
                        }
                    } else {
                        videoCall(context, dPhone)
                    }
                }
                if (showDialogPhone) {
                     DefaultSelection(
                        label = "phone number",
                        option = phone,
                        justOnce = { dPhone = it
                            when (numberUse) {
                                "Call" -> {
                                    call(context, dPhone)
                                }

                                "Message" -> {
                                    textMessage(context, dPhone)
                                }

                                "Video" -> {
                                    videoCall(context, dPhone)
                                }
                            }
                            dPhone = ""
                        },
                        always = { dPhone = it
                            detail.dPhone = dPhone
                            when (numberUse) {
                                "Call" -> {
                                    call(context, dPhone)
                                }

                                "Message" -> {
                                    textMessage(context, dPhone)
                                }

                                "Video" -> {
                                    videoCall(context, dPhone)
                                }
                            }
                            homeController.updateDefault(detail, id)
                        },
                        onDismiss = { showDialogPhone = false })
                }

                if (detail.email.isNotEmpty()) {
                    ImageBox(R.drawable.email, text = "Email") {
                        if (detail.dEmail == "") {
                            if (email.size > 1) {
                                showDialogEmail = true
                            } else {
                                email(context, email[0])
                            }
                        } else {
                            email(context, dEmail)
                        }
                    }
                }
                if (showDialogEmail) {
                    DefaultSelection(
                        label = "email",
                        option = email,
                        justOnce = { dEmail = it
                            email(context, dEmail)
                            dEmail = ""
                        },
                        always = { dEmail = it
                            detail.dEmail = dEmail
                            email(context, dEmail)
                            homeController.updateDefault(detail, id)
                        },
                        onDismiss = { showDialogEmail = false })
                }

                if (detail.address.isNotEmpty()) {
                    ImageBox(R.drawable.directions, text = "Direction") {
                        if (detail.dAddress == "") {
                            if (address.size > 1) {
                                showDialogAddress = true
                            } else {
                                direction(context, address[0])
                            }
                        } else {
                            direction(context, dAddress)
                        }
                    }
                }
                if (showDialogAddress) {
                    DefaultSelection(
                        label = "address",
                        option = address,
                        justOnce = { dAddress = it
                            direction(context, dAddress)
                            dAddress = ""
                        },
                        always = { dAddress = it
                            detail.dAddress = dAddress
                            direction(context, dAddress)
                            homeController.updateDefault(detail, id)
                        },
                        onDismiss = { showDialogAddress = false })
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
                    .background(color = boxColor, shape = RoundedCornerShape(15.dp)),
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Contact info",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                if(detail.phone.isEmpty() && detail.email.isEmpty() && detail.address.isEmpty()) {
                    AddPhoneOrEmail(R.drawable.phone, "phone number") {
                        navController.navigate(ScreenRoute.AddOrEditContact.route + "/${detail.id}")
                    }
                    AddPhoneOrEmail(R.drawable.email, "email") {
                        navController.navigate(ScreenRoute.AddOrEditContact.route + "/${detail.id}")
                    }
                }

                if (detail.phone.isNotEmpty()) {
                    for (i in phone.indices) {
                    Box {
                        var showMenuOnLongPress by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                                .combinedClickable(
                                    onClick = { call(context, phone[i]) },
                                    onLongClick = {
                                        pressFieldText = phone[i]
                                        showMenuOnLongPress = true
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically)
                        {
                            Column(
                                modifier = Modifier
                                    .weight(3f)
                            ) {
                                InfoRow(
                                    index = i,
                                    R.drawable.phone,
                                    default = { if(phone.size > 1) "+91 ${dPhone.take(5)} ${dPhone.takeLast(5)}" else ""},
                                    title = "+91 ${phone[i].take(5)} ${phone[i].takeLast(5)}",
                                    lPhone[i]
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.video),
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        videoCall(context, phone[i])
                                    }
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.message),
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        textMessage(context, phone[i])
                                    }
                                )
                            }
                        }
                        if (showMenuOnLongPress) {
                            ShowMenuOnLongPress(
                                context = context,
                                defaultText = dPhone,
                                text = pressFieldText,
                                defaultButton = phone.size > 1,
                                setDefault = {
                                    dPhone = if (phone.size > 1) {
                                        pressFieldText
                                    } else {
                                        ""
                                    }
                                    if (detail.dPhone.isNotEmpty() && detail.dPhone == pressFieldText) {
                                        dPhone = ""
                                    }
                                    detail.dPhone = dPhone
                                    homeController.updateDefault(detail, id)
                                },
                                onDismiss = { showMenuOnLongPress = false })
                        }
                    }
                }
                }

                if (detail.email.isNotEmpty()) {
                    for (i in email.indices) {
                        Box {
                            var showMenuOnLongPress by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(75.dp)
                                    .combinedClickable(
                                        onClick = { email(context, email[i]) },
                                        onLongClick = {
                                            pressFieldText = email[i]
                                            showMenuOnLongPress = true
                                        }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            )
                            {
                                InfoRow(
                                    index = i,
                                    R.drawable.email,
                                    default = { dEmail },
                                    email[i],
                                    lEmail[i]
                                )
                            }
                            if (showMenuOnLongPress) {
                                ShowMenuOnLongPress(
                                    context = context,
                                    defaultText = dEmail,
                                    text = pressFieldText,
                                    defaultButton = email.size > 1,
                                    setDefault = {
                                        dEmail = if (email.size > 1) {
                                            pressFieldText
                                        } else {
                                            ""
                                        }
                                        if (detail.dEmail.isNotEmpty() && detail.dEmail == pressFieldText) {
                                            dEmail = ""
                                        }
                                        detail.dEmail = dEmail
                                        homeController.updateDefault(detail, id)
                                    },
                                    onDismiss = { showMenuOnLongPress = false })
                            }
                        }
                    }
                }

                if (detail.address.isNotEmpty()) {
                    for (i in address.indices) {
                        Box {
                            var showMenuOnLongPress by remember { mutableStateOf(false) }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(75.dp)
                                    .combinedClickable(
                                        onClick = {
                                            val geoUri = "geo:0,0?q=${address[i]}"
                                            val mapIntent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                                            mapIntent.setPackage("com.google.android.apps.maps")
                                            startActivity(context, mapIntent, null)
                                        },
                                        onLongClick = {
                                            pressFieldText = address[i]
                                            showMenuOnLongPress = true
                                        }
                                    ),
                                verticalAlignment = Alignment.CenterVertically)
                            {
                                Column(
                                    modifier = Modifier
                                        .weight(3.5f)
                                )
                                {
                                    InfoRow(
                                        index = i,
                                        R.drawable.address,
                                        default = { dAddress },
                                        address[i],
                                        lAddress[i]
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 10.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.directions),
                                        contentDescription = null,
                                        modifier = Modifier.clickable {
                                            direction(context, address[i])
                                        }
                                    )
                                }
                            }
                            if (showMenuOnLongPress) {
                                ShowMenuOnLongPress(
                                    context = context,
                                    defaultText = dAddress,
                                    text = pressFieldText,
                                    defaultButton = address.size > 1,
                                    setDefault = {
                                        dAddress = if (address.size > 1) {
                                            pressFieldText
                                        } else {
                                            ""
                                        }
                                        if (detail.dAddress.isNotEmpty() && detail.dAddress == pressFieldText) {
                                            dAddress = ""
                                        }
                                        detail.dAddress = dAddress
                                        homeController.updateDefault(detail, id)
                                    },
                                    onDismiss = { showMenuOnLongPress = false })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (detail.birthDate.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp)
                        .background(color = boxColor, shape = RoundedCornerShape(15.dp)),
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "About ${detail.firstname} ${detail.surname}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp)
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    if (detail.birthDate.isNotEmpty()) {
                        InfoRow(
                            index = 0,
                            R.drawable.birthday,
                            default = { "" },
                            detail.birthDate,
                            "Birthday"
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}