package com.example.app8.view.component

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import com.example.app8.ui.theme.dialogColor
import com.example.app8.ui.theme.theme
import com.example.app8.ui.theme.themeColor
import com.example.app8.ui.theme.themeColorLight

@Composable
fun ImageBox(image: Int, text: String, onclick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier.clickable(onClick = { onclick?.invoke() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.background(color = themeColor, shape = CircleShape)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = themeColorLight),
                modifier = Modifier
                    .padding(20.dp)
                    .size(20.dp)
            )
        }
        Text(text = text)
    }
}

@Composable
fun InfoRow(index: Int, iconRes: Int, default: () -> String, title: String, subtitle: String) {
    Row {
        if (index == 0) {
            Icon(
                painterResource(iconRes),
                contentDescription = null,
                Modifier.padding(start = 20.dp, top = 10.dp)
            )
        } else {
            Spacer(modifier = Modifier.width(45.dp))
        }
        Column(Modifier.padding(start = 15.dp)) {
            Text(title, fontSize = 20.sp)
            Row {
                Text(subtitle, fontSize = 15.sp)
                if (default.invoke() == title) {
                    Text(" - Default", fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun DefaultSelection(
    label: String,
    option: List<String>,
    justOnce: (String) -> Unit,
    always: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismiss.invoke() }) {
        Column(
            modifier = Modifier.background(color = dialogColor, shape = RoundedCornerShape(25.dp))
        ) {
            Text(
                text = "Choose $label",
                fontSize = 25.sp,
                modifier = Modifier.padding(start = 15.dp, top = 20.dp)
            )
            Column(modifier = Modifier.selectableGroup()) {
                option.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (text == selectedOption), onClick = null)
                        Text(
                            text = text, fontSize = 18.sp, modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
            }

            Row(modifier = Modifier.padding(start = 150.dp)) {
                TextButton(onClick = {
                    justOnce.invoke(selectedOption)
                    onDismiss.invoke()
                }) { Text(text = "Just once") }
                TextButton(onClick = {
                    always.invoke(selectedOption)
                    onDismiss.invoke()
                }) { Text(text = "Always") }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun ShowMenuOnLongPress(
    context: Context,
    defaultText: String,
    text: String,
    defaultButton: Boolean,
    setDefault: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true, onDismissRequest = { onDismiss.invoke() }) {
        DropdownMenuItem(text = { Text(text = "Copy to clipboard") }, onClick = {
            onDismiss.invoke()
            val clipboardManager = getSystemService(context, ClipboardManager::class.java)
            val clip = ClipData.newPlainText("label", text)
            clipboardManager?.setPrimaryClip(clip)
            Toast.makeText(
                context, "copied!!", Toast.LENGTH_SHORT
            ).show()
        })
        if (defaultButton) {
            DropdownMenuItem(
                text = { Text(text = if (text == defaultText) "clear default" else "set default") },
                onClick = {
                    onDismiss.invoke()
                    setDefault.invoke()
                })
        }
    }
}

@Composable
fun AddPhoneOrEmail(icon: Int, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 15.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Icon(
            painterResource(icon),
            contentDescription = null,
            tint = theme,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            "Add $text",
            fontSize = 20.sp,
            color = theme,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
}

fun call(context: Context, phoneNo: String) {
    val granted = checkPermission((context as Activity), Manifest.permission.CALL_PHONE)
    if (granted) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:$phoneNo".toUri()
        }
        context.startActivity(intent)
    }
}

fun textMessage(context: Context, phoneNo: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "smsto:${phoneNo}".toUri()
    }
    context.startActivity(intent)
}

fun videoCall(context: Context, phoneNo: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = "tel:$phoneNo".toUri()
        setPackage("com.google.android.apps.tachyon")
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Duo not installed", Toast.LENGTH_SHORT).show()
    }
}

fun email(context: Context, emailId: String) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW, "mailto:$emailId".toUri()
        )
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
    }
}

fun direction(context: Context, address: String) {
    val uri = "https://www.google.com/maps/dir/?api=1&destination=${
        Uri.encode(address)
    }".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}