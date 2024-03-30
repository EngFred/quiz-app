package com.engineerfred.kotlin.ktor.util

import android.content.Context
import android.content.Intent
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

fun restartApp( context: Context ) {
    val pm = context.packageManager
    val intent = pm.getLaunchIntentForPackage(context.packageName)
    val componentName = intent!!.component
    val restartIntent = Intent.makeRestartActivityTask(componentName)
    context.startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}