package com.bangkit.android.dermatify.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bangkit.android.dermatify.R
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar

// View Extension Function
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
