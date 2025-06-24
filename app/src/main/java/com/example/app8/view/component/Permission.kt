package com.example.app8.view.component

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat.checkSelfPermission

fun checkPermission(
    activity: Activity,
    permission: String
): Boolean {
    val isGranted = checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    if (isGranted) return true

    showPermissionSettingsDialog(activity, permission)
    return false
}

fun showPermissionSettingsDialog(activity: Activity, permission: String) {
    AlertDialog.Builder(activity)
        .setTitle("Permission Required")
        .setMessage(
            when (permission) {
                Manifest.permission.CAMERA -> {
                    "This permission is needed to pick images from camera. Please enable it in App Settings."
                }

                Manifest.permission.READ_MEDIA_IMAGES -> {
                    "This permission is needed to pick images. Please enable it in App Settings."
                }

                Manifest.permission.CALL_PHONE -> {
                    "This permission is needed to call. Please enable it in App Settings."
                }

                else -> {
                    ""
                }
            }
        )
        .setPositiveButton("Go to Settings") { _, _ ->
            val intent =
                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
            activity.startActivity(intent)
        }
        .setNegativeButton("Cancel", null)
        .show()
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UseKtx")
@Composable
fun PermissionHandler(context: Context) {

    val permissions = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    )

    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        /*val deniedPermissions = permissionsResult.filterValues { !it }.keys
         if (deniedPermissions.isEmpty()) {
             Toast.makeText(context, "All permissions granted", Toast.LENGTH_SHORT).show()
         } else {
             Toast.makeText(context, "Denied: ${deniedPermissions.joinToString()}", Toast.LENGTH_LONG).show()
         }*/
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = permissions.filter {
            checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsToRequest.isNotEmpty()) {
            multiplePermissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
//            Toast.makeText(context, "All permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }
}