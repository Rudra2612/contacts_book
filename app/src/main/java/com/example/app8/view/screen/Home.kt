package com.example.app8.view.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.app8.R
import com.example.app8.controller.HomeController
import com.example.app8.model.ContactModel
import com.example.app8.view.component.ContactList
import com.example.app8.view.component.PermissionHandler
import com.example.app8.view.navigation.ScreenRoute

@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavHostController) {

    val context = LocalContext.current
    PermissionHandler(context)

    val homeController by lazy { HomeController(context) }
    val contactList = homeController.getAllContacts()

    contactList.sortWith { o1, o2 ->
        o1.firstname.compareTo(o2.firstname, ignoreCase = true)
    }
    contactList.sortWith { o1, o2 ->
        if (!o1.firstname.first().isLetterOrDigit()) {
            return@sortWith -1
        } else 0
    }
    var character = ""
    contactList.forEach { contact ->
        val firstChar = contact.firstname.first().lowercaseChar()

        val label = when {
            firstChar.isLetter() -> firstChar.toString()
            firstChar.isDigit() ->  "#"
            else -> "..."
        }
        if (character != label) {
            character = label
            contact.label = label
        }
    }

    val items = remember { mutableStateListOf<ContactModel>() }
    items.clear()
    for (i in 0 until contactList.size) {
        items.add(contactList[i])
    }

    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val filteredItems = items.filter { it.firstname.contains(searchQuery, ignoreCase = true) }

    val voiceLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        activityResult.data?.let { data ->
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            searchQuery = results?.get(0) ?: "None"
        }
    }

    val isSelectionMode by remember { derivedStateOf { items.any { it.selected.value == "true" } } }
    val count by remember { derivedStateOf { items.count { it.selected.value == "true" } } }
    var showDropMenu by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenRoute.AddOrEditContact.route + "/")
                },
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            if (!isSelectionMode) {
                val keyboardController = LocalSoftwareKeyboardController.current
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { keyboardController?.hide() },
                            expanded = active,
                            onExpandedChange = { active = it },
                            placeholder = { Text("Search") },
                            leadingIcon = {
                                if (true) {
                                    if (active) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.clickable {
                                                searchQuery = ""
                                                active = false
                                            }
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            },
                            trailingIcon = {
                                if (true) {
                                    Row {
                                        if (searchQuery.isNotEmpty()) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(end = 15.dp)
                                                    .clickable {
                                                        searchQuery = ""
                                                    }
                                            )
                                        }
                                        val voiceIntent =
                                            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                                putExtra(
                                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                                )
                                            }
                                        if (active) {
                                            Icon(
                                                painter = painterResource(R.drawable.microphone),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(end = 25.dp)
                                                    .clickable {
                                                        voiceLauncher.launch(voiceIntent)
                                                    }
                                            )
                                        }
                                    }
                                } else {
                                    Row {
                                        Icon(
                                            painter = painterResource(R.drawable.share),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 15.dp)
                                        )
                                        Icon(
                                            painter = painterResource(R.drawable.delete),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 25.dp)
                                        )
                                    }
                                }
                            },
                            interactionSource = null,
                        )
                    },
                    expanded = active,
                    modifier = Modifier
                        .padding(
                            start = if (active) 0.dp else 10.dp,
                            end = if (active) 0.dp else 10.dp,
                            bottom = 20.dp
                        )
                        .fillMaxWidth(),
                    onExpandedChange = { active = it },
                    shape = SearchBarDefaults.inputFieldShape,
                    shadowElevation = SearchBarDefaults.ShadowElevation,
                    content = {
                        if (searchQuery != "") {
                           ContactList(
                                contactList = filteredItems,
                                navController = navController,
                                innerpadding = null,
                                isSearch = true,
                                context = context,
                               onSelectionChanged = {
                                   homeController.contactSelected(it, it.id)
                               }
                            )
                        }
                    },
                )
            } else {
                TopAppBar(
                    title = {  Text("$count selected") },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    items.forEach {
                                        it.selected.value = "false"
                                    }
                                    showDropMenu = false
                                }
                        )
                    },
                    actions = {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 20.dp)
                                    .clickable {
                                        val selectedContacts = items.filter { it.selected.value == "true" }

                                        if (selectedContacts.isNotEmpty()) {
                                            val shareText = selectedContacts.joinToString("\n") { contact ->
                                                "${contact.firstname} ${contact.surname}\n" +
                                                        "Phone: ${contact.phone.replace("_0_", ",\n             ")}"
                                            }

                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, shareText)
                                                type = "text/plain"
                                            }
                                            val shareIntent = Intent.createChooser(sendIntent, "Share")

                                            context.startActivity(shareIntent)
                                        }
                                        items.forEach {
                                            it.selected.value = "false"
                                        }
                                    }
                            )
                            Icon(
                                painter = painterResource(R.drawable.delete),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 15.dp)
                                    .clickable {
                                        val toDelete = items.filter { it.selected.value == "true" }
                                        toDelete.forEach { homeController.deleteContact(it.id) }
                                        items.removeAll(toDelete)
                                    }
                            )
                            if(!(items.all { it.selected.value == "true" })) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .clickable { showDropMenu = true }
                                )
                            }
                            DropdownMenu(
                                expanded = showDropMenu,
                                onDismissRequest = { showDropMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Select All") },
                                    onClick = {
                                        showDropMenu = false
                                        items.forEach {
                                            it.selected.value = "true"
                                            homeController.contactSelected(it, it.id)
                                        }
                                    })
                            }
                        }
                    }
                )
            }
        }) { innerPadding ->

        ContactList(
            contactList = contactList,
            navController = navController,
            innerpadding = innerPadding,
            isSearch = false,
            context = context,
            onSelectionChanged = {
                homeController.contactSelected(it, it.id)
            }
        )
    }
}