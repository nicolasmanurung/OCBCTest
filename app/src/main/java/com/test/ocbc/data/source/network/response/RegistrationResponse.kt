package com.test.ocbc.data.source.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegistrationResponse(
    val status: String,
    val token: String? = "",
    val error: String? = ""
) : Parcelable