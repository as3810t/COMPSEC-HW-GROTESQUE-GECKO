package com.example.grotesquegecko.ui.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.grotesquegecko.R
import com.example.grotesquegecko.injector


fun glideLoader(
    context: Context,
    imageView: ImageView,
    url: String?,
) {

    val token = context?.injector!!.getToken().getToken()

    val glideUrl = GlideUrl(
        url,
        LazyHeaders.Builder()
            .addHeader("Authorization", "Bearer ${token!!}")
            .addHeader("Accept", "image/bmp")
            .build()
    )

    Glide.with(context)
        .load(glideUrl)
        .placeholder(R.drawable.icon_gecko)
        .into(imageView)
}