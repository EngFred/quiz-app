package com.engineerfred.kotlin.ktor.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

fun Context.compressImage( filePath: String): ByteArray {
    // compressing image
    val bitmap = MediaStore.Images.Media.getBitmap( contentResolver, Uri.parse( filePath ) )
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}