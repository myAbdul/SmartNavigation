package com.example.smartnavigation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

fun decodeImage(image: Bitmap): String = image.scaleImage().toByteArray().base64

fun decodeImage(image: String): Bitmap = image.fromBase64().toBitmap()

private fun Bitmap.scaleImage(): Bitmap {
    val maxImageSize = 512F
    val ratio = (maxImageSize / width).coerceAtMost(maxImageSize / height)
    val width = (ratio * width).roundToInt()
    val height = (ratio * height).roundToInt()
    return try {
        val resultBitmap = Bitmap.createScaledBitmap(
            this, width,
            height, true
        )
        resultBitmap
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        this
    }
}

private fun Bitmap.toByteArray(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 80
): ByteArray = ByteArrayOutputStream().use {
    compress(format, quality, it)
    it.toByteArray()
}

private val ByteArray.base64: String get() = Base64.encodeToString(this, Base64.DEFAULT)

private fun ByteArray.toBitmap() = BitmapFactory.decodeByteArray(this, 0, this.size)

private fun String.fromBase64(): ByteArray = Base64.decode(this, Base64.DEFAULT)