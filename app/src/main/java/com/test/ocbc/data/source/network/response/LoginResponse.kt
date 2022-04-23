package com.test.ocbc.data.source.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    val accountNo: String? = "",
    val status: String? = "",
    val token: String? = "",
    val username: String? = "",
    val error: String? = ""
) : Parcelable