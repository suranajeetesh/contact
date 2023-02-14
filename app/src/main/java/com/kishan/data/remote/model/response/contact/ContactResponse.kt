package com.kishan.data.remote.model.response.contact

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactResponse(
    var age: Int? = null,
    var company: String? = null,
    var email: String? = null,
    var first_name: String? = null,
    var gender: String? = null,
    var id: String? = null,
    var last_name: String? = null,
    var name: String? = null,
    var photo: String? = null
): Parcelable