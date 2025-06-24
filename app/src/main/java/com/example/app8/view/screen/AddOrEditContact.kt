package com.example.app8.view.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.app8.R
import com.example.app8.controller.HomeController
import com.example.app8.model.ContactModel
import com.example.app8.ui.theme.circleColor
import com.example.app8.ui.theme.theme
import com.example.app8.ui.theme.themeColor
import com.example.app8.view.component.FieldAddButton
import com.example.app8.view.component.InputTextField
import com.example.app8.view.component.RemoveField
import com.example.app8.view.component.colorToHexString
import com.example.app8.view.component.dropdownMenuBox
import com.example.app8.view.component.imagePicker
import com.example.app8.view.component.inputTextField
import com.example.app8.view.component.inputTextFieldWithDatePicker
import com.example.app8.view.component.saveUriToCache
import com.example.app8.view.navigation.ScreenRoute


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AddOrEditContact(navController: NavHostController, id: Int?) {

    val context = LocalContext.current
    val homeController by lazy { HomeController(context) }
    val contactModel = id?.let { homeController.getContactDetail(it) } ?: run { ContactModel() }

    var showDropMenu by remember { mutableStateOf(false) }
    var birthdayShow by remember { mutableStateOf(contactModel.birthDate.isNotEmpty()) }
    var imagePath by remember { mutableStateOf(contactModel.image) }

    val emailList: List<String> = if (contactModel.email.isNotEmpty()) {
        contactModel.email.split("_0_")
    } else {
        emptyList()
    }
    val lEmailList: List<String> = if (contactModel.lEmail.isNotEmpty()) {
        contactModel.lEmail.split("_0_")
    } else {
        emptyList()
    }
    val phoneList: List<String> = if (contactModel.phone.isNotEmpty()) {
        contactModel.phone.split("_0_")
    } else {
        listOf("")
    }
    val lPhoneList: List<String> = if (contactModel.lPhone.isNotEmpty()) {
        contactModel.lPhone.split("_0_")
    } else {
        listOf("")
    }
    val addressList: List<String> = if (contactModel.address.isNotEmpty()) {
        contactModel.address.split("_0_")
    } else {
        emptyList()
    }
    val lAddressList: List<String> = if (contactModel.lAddress.isNotEmpty()) {
        contactModel.lAddress.split("_0_")
    } else {
        emptyList()
    }


    val phone = remember { mutableStateListOf<String>().apply { addAll(phoneList) } }
    val lPhone = remember { mutableStateListOf<String>().apply { addAll(lPhoneList) } }
    val email = remember { mutableStateListOf<String>().apply { addAll(emailList) } }
    val lEmail = remember { mutableStateListOf<String>().apply { addAll(lEmailList) } }
    val address = remember { mutableStateListOf<String>().apply { addAll(addressList) } }
    val lAddress = remember { mutableStateListOf<String>().apply { addAll(lAddressList) } }

    if (phone.size == 1) {
        contactModel.dPhone = ""
    }
    if (email.size == 1) {
        contactModel.dEmail = ""
    }
    if (address.size == 1) {
        contactModel.dAddress = ""
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                imagePath = saveUriToCache(context = context, it)?.absolutePath ?: ""
            }

        }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(if (id == null) "Create contact" else "Edit contact") },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp)
                        .clickable {
                            id?.let {
                                navController.navigate(ScreenRoute.ContactDetails.route + "/${id}") {
                                    popUpTo(ScreenRoute.AddOrEditContact.route) {
                                        inclusive = true
                                    }
                                }
                            } ?: run {
                                navController.navigate(ScreenRoute.Home.route) {
                                    popUpTo(ScreenRoute.AddOrEditContact.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        })
            },
            actions = {
                Button(
                    onClick = {
                        if ((phone != listOf("")) && (contactModel.firstname != "")) {
                            contactModel.color = colorToHexString(circleColor.random())
                            contactModel.phone = phone.joinToString("_0_")
                            contactModel.lPhone = lPhone.joinToString("_0_")
                            contactModel.email = email.joinToString("_0_")
                            contactModel.lEmail = lEmail.joinToString("_0_")
                            contactModel.address = address.joinToString("_0_")
                            contactModel.lAddress = lAddress.joinToString("_0_")
                            contactModel.image = imagePath
                            contactModel.selected.value = "false"
                            if (contactModel.phone.length == 10) {
                                contactModel.dPhone = contactModel.phone
                            } else {
                                contactModel.dPhone = ""
                            }
                            id?.let {
                                homeController.updateContact(contactModel, id)
                                navController.navigate(ScreenRoute.ContactDetails.route + "/${id}") {
                                    popUpTo(ScreenRoute.AddOrEditContact.route) {
                                        inclusive = true
                                    }
                                }
                            } ?: run {
                                contactModel.favorite = "false"
                                homeController.insertContact(contactModel)
                                navController.navigate(ScreenRoute.Home.route) {
                                    popUpTo(ScreenRoute.AddOrEditContact.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        } else {
                            navController.navigate(ScreenRoute.Home.route) {
                                popUpTo(ScreenRoute.AddOrEditContact.route) {
                                    inclusive = true
                                }
                            }
                        }

                    },
                    modifier = Modifier.padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = theme
                    )
                ) { Text("Save") }
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable {
                            id?.let {
                                showDropMenu = true
                            }
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
                            homeController.deleteContact(id!!)
                            navController.navigate(ScreenRoute.Home.route) {
                                popUpTo(ScreenRoute.AddOrEditContact.route) {
                                    inclusive = true
                                }
                            }
                        })
                }
            })
    }) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(color = themeColor, shape = CircleShape)
                    .clickable { imagePicker(context, launcher) }
            ) {
                GlideImage(
                    model = imagePath.ifEmpty { R.drawable.add_image },
                    contentDescription = null,
                    colorFilter = if (imagePath.isEmpty()) ColorFilter.tint(Color.White) else null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(if (imagePath.isEmpty()) 45.dp else 0.dp)
                        .clip(shape = RoundedCornerShape(if (imagePath.isEmpty()) 0.dp else 130.dp))
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (imagePath == "") {
                Text(
                    text = "Add picture",
                    color = theme,
                    modifier = Modifier.clickable { imagePicker(context, launcher) })
            } else {
                Row {
                    Text(
                        text = "Change",
                        color = theme,
                        modifier = Modifier.clickable { imagePicker(context, launcher) })
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = "Remove",
                        color = theme,
                        modifier = Modifier.clickable {
                            imagePath = ""
                            contactModel.image = ""
                        })
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            contactModel.firstname =
                inputTextField(labelName = "First name", text = contactModel.firstname)

            Spacer(modifier = Modifier.height(10.dp))

            contactModel.surname =
                inputTextField(labelName = "Surname", text = contactModel.surname)

            Spacer(modifier = Modifier.height(20.dp))

            contactModel.company =
                inputTextField(labelName = "Company", text = contactModel.company)


            if (phone.isNotEmpty()) {
                phone.forEachIndexed { index, s: String ->
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(modifier = Modifier.padding(start = 30.dp)) {
                        InputTextField(text = s, labelName = "Phone") { phone[index] = it }

                        Spacer(modifier = Modifier.width(10.dp))

                        RemoveField {
                            phone.removeAt(index)
                            lPhone.removeAt(index)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    lPhone[index] = dropdownMenuBox(
                        arrayOf("Mobile", "Home", "Work", "Custom"),
                        labelText = lPhone[index].ifEmpty { "Mobile" }
                    )
                }

                Text(
                    "Add phone",
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 55.dp, top = 8.dp)
                        .clickable {
                            phone.add("")
                            lPhone.add("")
                        })
            } else {
                Spacer(modifier = Modifier.height(15.dp))
                FieldAddButton("Add phone", R.drawable.phone) {
                    phone.add("")
                    lPhone.add("")
                }
            }

            if (email.isNotEmpty()) {
                email.forEachIndexed { index, s: String ->
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(modifier = Modifier.padding(start = 30.dp)) {
                        InputTextField(text = s, labelName = "Email") { email[index] = it }

                        Spacer(modifier = Modifier.width(10.dp))

                        RemoveField {
                            email.removeAt(index)
                            lEmail.removeAt(index)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    lEmail[index] = dropdownMenuBox(
                        arrayOf("Home", "Work", "Custom"),
                        labelText = lEmail[index].ifEmpty { "Home" }
                    )
                }

                Text(
                    "Add email",
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 55.dp, top = 8.dp)
                        .clickable {
                            email.add("")
                            lEmail.add("")
                        })
            } else {
                Spacer(modifier = Modifier.height(15.dp))
                FieldAddButton("Add email", R.drawable.email) {
                    email.add("")
                    lEmail.add("")
                }
            }

            if (birthdayShow) {
                Spacer(modifier = Modifier.height(15.dp))

                Row(modifier = Modifier.padding(start = 30.dp)) {
                    contactModel.birthDate =
                        inputTextFieldWithDatePicker(text = contactModel.birthDate)

                    Spacer(modifier = Modifier.width(10.dp))

                    RemoveField { birthdayShow = false }
                }

                Spacer(modifier = Modifier.height(5.dp))
            } else {
                Spacer(modifier = Modifier.height(15.dp))
                FieldAddButton("Add birthday", R.drawable.birthday) { birthdayShow = true }
            }

            if (address.isNotEmpty()) {
                address.forEachIndexed { index, s: String ->
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(modifier = Modifier.padding(start = 30.dp)) {
                        InputTextField(text = s, labelName = "Address") { address[index] = it }

                        Spacer(modifier = Modifier.width(10.dp))

                        RemoveField {
                            address.removeAt(index)
                            lAddress.removeAt(index)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    lAddress[index] = dropdownMenuBox(
                        arrayOf("Home", "Work", "Custom"),
                        labelText = lAddress[index].ifEmpty { "Home" }
                    )
                }

                Text(
                    "Add Address",
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 55.dp, top = 8.dp)
                        .clickable {
                            address.add("")
                            lAddress.add("")
                        })
            } else {
                Spacer(modifier = Modifier.height(15.dp))
                FieldAddButton("Add address", R.drawable.address) {
                    address.add("")
                    lAddress.add("")
                }
            }

        }
    }
}