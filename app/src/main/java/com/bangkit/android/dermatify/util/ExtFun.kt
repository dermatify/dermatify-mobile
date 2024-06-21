package com.bangkit.android.dermatify.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.bangkit.android.dermatify.BuildConfig
import com.bangkit.android.dermatify.R
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.random.Random

private const val MAXIMAL_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.ROOT).format(Date())


// Date
fun formatDate(createdAt: String): String {
    val postDate = Instant.parse(createdAt)
    val systemTimezone = TimeZone.currentSystemDefault()
    val customFormat = LocalDateTime.Format {
        dayOfMonth()
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        year()
        char(',')
        char(' ')
        hour()
        char(':')
        minute()
        char(' ')
        amPmMarker("AM", "PM")
    }
    val toLocalDateTime = postDate.toLocalDateTime(systemTimezone)
    return customFormat.format(toLocalDateTime)
}

// Get Uri from Media Store
fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${timeStamp}_facial_skin_scan_${UUID.randomUUID()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/${timeStamp}_facial_skin_scan_${UUID.randomUUID()}.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}

// Uri To String
fun Uri?.convertUriToString(): String {
    return this?.toString() ?: ""
}

// Uri to File
fun Uri.uriToFile(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val file = File(context.cacheDir, "profilePic_${UUID.randomUUID()}_$timeStamp.jpg")
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return file
}

// Image File Extension Function
fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

private fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}


// View Extension Function
fun ImageView.setUriToImageView(userProfilePic: Uri?) {
    userProfilePic?.let {
        this.setImageURI(it)
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
fun Fragment.closeKeyboard() {
    view?.let {activity?.hideKeyboard(it)}
}

fun View.showSnackbar(message: String, type: String = "", anchorView: View = this) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).setAnchorView(anchorView)
        .setBackgroundTint(
            ContextCompat.getColor(
                context,
                when (type) {
                    "success" -> R.color.green_success
                    else -> R.color.red_error
                }
            )
        )
        .setTextColor(context.getColor(R.color.white))
        .show()
}

fun View.showSnackbarWithActionBtn(message: String, type: String = "", actionMsg: String, onClick: () -> Unit, anchorView: Int = R.id.fabBotNav) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_INDEFINITE
    ).setBackgroundTint(
            ContextCompat.getColor(
                context,
                when (type) {
                    "success" -> R.color.green_success
                    else -> R.color.red_error
                }
            )
        )
        .setAction(actionMsg) {
            onClick()
        }
        .setAnchorView(anchorView)
        .setTextColor(context.getColor(R.color.white))
        .setActionTextColor(context.getColor(R.color.white))
        .show()
}

fun ShimmerFrameLayout.showShimmer() {
    this.visible()
    this.startShimmer()
    this.showShimmer(true)
}

fun ShimmerFrameLayout.goneShimmer() {
    this.gone()
    this.stopShimmer()
    this.showShimmer(false)
}

fun ImageView.loadImg(url: String) {
    Glide.with(this.context)
        .load(url)
        .error(
            Glide.with(this.context)
                .load(url)
        )
        .centerCrop()
        .into(this)
}
