package com.kishan.util.bindingAdapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.example.newbasicstructure.R
import com.kishan.data.remote.model.response.contact.ContactResponse

/**
 * Created by Jeetesh Surana.
 */

@BindingAdapter("setProfile")
fun setProfile(
    imageView: AppCompatImageView,
    mediaUrl: String?
) {
    imageView.setPicture(
        mediaUrl,
        RequestOptions().placeholder(R.drawable.app_icon).circleCrop()
    )
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setContactName")
fun setContactName(
    name: TextView,
    data: ContactResponse?
) {
    name.text = "${data?.first_name} ${data?.last_name}"
}
