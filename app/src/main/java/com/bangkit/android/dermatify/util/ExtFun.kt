package com.bangkit.android.dermatify.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout

// View Extension Function
fun View.hide() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun ShimmerFrameLayout.showShimmer() {
    this.visible()
    this.startShimmer()
    this.showShimmer(true)
}

fun ShimmerFrameLayout.goneShimmer() {
    this.hide()
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
