package com.project.qa.ui_components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import com.project.qa.utils.createImageFile

@Composable
fun ChooseImageDialogue(
    openChooseDialogue: MutableState<Boolean>,
    resultLauncherGallery: ManagedActivityResultLauncher<String, Uri?>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    context: Context,
    uri : MutableState<Uri>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
) {
  val file = context.createImageFile()
    val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
    val builder = android.app.AlertDialog.Builder(context)
    builder.setTitle("Add Photo!")
    builder.setItems(options) { dialog, item ->
        if (options[item] == "Take Photo") {
            try {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri.value)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (options[item] == "Choose from Gallery") {
            resultLauncherGallery.launch("image/*")
        } else if (options[item] == "Cancel") {
            dialog.dismiss()
        }
    }
    builder.show()
    openChooseDialogue.value = false
}