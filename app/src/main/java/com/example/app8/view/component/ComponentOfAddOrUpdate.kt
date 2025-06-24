package com.example.app8.view.component

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.app8.R
import com.example.app8.ui.theme.dialogColor
import com.example.app8.ui.theme.minusColor
import com.example.app8.ui.theme.themeColor
import com.example.app8.utils.toStringFormat
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import kotlin.random.Random

@Composable
fun InputTextField(text: String, labelName: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = when (labelName) {
                "Phone" -> KeyboardType.Phone
                "Email" -> KeyboardType.Email
                else -> KeyboardType.Text
            }, imeAction = ImeAction.Next
        ),
        singleLine = true,
        modifier = Modifier.width(300.dp),
        label = { Text(text = labelName, fontSize = 17.sp) },
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 20.sp),
    )
}

@Composable
fun inputTextField(labelName: String, text: String): String {

    var text1 by remember { mutableStateOf(text) }

    OutlinedTextField(
        value = text1,
        onValueChange = {
            text1 = it
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        modifier = Modifier.width(300.dp),
        label = { Text(text = labelName, fontSize = 17.sp) },
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 20.sp),
    )
    return text1.trim()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun inputTextFieldWithDatePicker(text: String): String {

    var text1 by remember { mutableStateOf(text) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(onDismissRequest = {
            showDatePicker = false
        }, confirmButton = {
            TextButton(onClick = {
                showDatePicker = false
                val dd = Date(datePickerState.selectedDateMillis!!)
                val format = dd.toStringFormat()
                text1 = format
            }) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDatePicker = false
            }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value = text1,
        onValueChange = {
            text1 = it
        },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
        },
        modifier = Modifier.width(300.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        label = { Text(text = "Birthday", fontSize = 17.sp) },
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 20.sp),
    )
    return text1
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun imagePicker(context: Context, launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    val activity = context as? Activity ?: return
    var grantedI by mutableStateOf(false)
    var grantedC by mutableStateOf(false)

    grantedI = checkPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
    if (grantedI) {
        grantedC = checkPermission(activity, Manifest.permission.CAMERA)
    }
    if (grantedI && grantedC) {
        ImagePicker.with(activity).cropSquare().compress(1024).maxResultSize(1080, 1080)
            .createIntent {
                launcher.launch(it)
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownMenuBox(option: Array<String>, labelText: String): String {

    var expanded by remember { mutableStateOf(false) }
    var custom by remember { mutableStateOf(false) }
    var label by remember { mutableStateOf("") }
    var selectedText by remember { mutableStateOf(labelText) }

    Box {
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = {
                expanded = !expanded
            }) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .width(300.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                option.forEach { item ->
                    DropdownMenuItem(text = { Text(text = item) }, onClick = {
                        if (item == "Custom") {
                            custom = true
                        } else {
                            selectedText = item
                        }
                        expanded = false
                    })
                }
            }
        }
    }
    if (custom) {
        Dialog(onDismissRequest = {
            if (label.isNotEmpty()) selectedText = label
            label = ""
            custom = false
        }) {
            Column(
                modifier = Modifier
                    .background(
                        color = dialogColor, shape = RoundedCornerShape(25.dp)
                    )
                    .size(width = 350.dp, height = 200.dp)
            ) {
                Text(
                    text = "Custom label name",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 15.dp, top = 20.dp)
                )
                OutlinedTextField(
                    value = label,
                    onValueChange = {
                        label = it
                    },
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp),
                    label = { Text(text = "Label name", fontSize = 17.sp) },
                    shape = RoundedCornerShape(10.dp),
                    textStyle = TextStyle(fontSize = 25.sp),
                )
                Row(modifier = Modifier.padding(start = 180.dp, top = 20.dp)) {
                    TextButton(onClick = {
                        label = ""
                        custom = false
                    }) { Text(text = "Cancel") }
                    TextButton(
                        onClick = {
                            if (label.isNotEmpty()) selectedText = label
                            label = ""
                            custom = false
                        }, enabled = label.isNotEmpty()
                    ) { Text(text = "Ok") }

                }
            }
        }
    }
    return selectedText
}

@Composable
fun RemoveField(onclick: () -> Unit) {
    Icon(
        painter = painterResource(R.drawable.minus),
        contentDescription = null,
        tint = minusColor,
        modifier = Modifier
            .padding(top = 25.dp)
            .clickable(onClick = onclick)
    )
}

@Composable
fun FieldAddButton(text: String, image: Int, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 45.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = themeColor, contentColor = Color.White
        )
    ) {
        Icon(painter = painterResource(image), contentDescription = null)
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

fun saveUriToCache(context: Context, uri: Uri): File? {
    return try {
        val fileName = "${Random.nextLong()}.jpg"
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val cacheFile = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(cacheFile)

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        cacheFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun colorToHexString(color: Color): String {
    return "#%02X%02X%02X%02X".format(
        (color.alpha * 255).toInt(),
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )
}