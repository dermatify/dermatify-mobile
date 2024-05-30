package com.bangkit.android.dermatify.util

import android.view.View

// View Extension Function
fun View.hide() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}