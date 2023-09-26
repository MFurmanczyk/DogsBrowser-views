package com.mfurmanczyk.dogsbrowser.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load

const val PERMISSION_SEND_SMS = 1000

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

@BindingAdapter("android:imageUrl")
fun loadImage (view: ImageView, url: String?) {
    view.load(url) {
        placeholder(getProgressDrawable(view.context))
    }
}