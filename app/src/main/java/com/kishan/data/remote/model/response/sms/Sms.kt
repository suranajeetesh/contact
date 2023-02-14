package com.kishan.data.remote.model.response.sms

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sms(val address: String, val body: String, val date: String, val time: String):
    Parcelable
