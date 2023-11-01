package com.project.qa.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun uriToBitmap(contentResolver: ContentResolver, selectedFileUri: Uri): Bitmap {
    val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(
            ImageDecoder.createSource(contentResolver, selectedFileUri)
        ) { decoder, info, source ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
        }
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, selectedFileUri)
    }
    return bitmap
}