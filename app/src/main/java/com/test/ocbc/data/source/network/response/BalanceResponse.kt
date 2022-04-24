package com.test.ocbc.data.source.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BalanceResponse(
    val accountNo: String? = "",
    val balance: Double? = 0.0,
    val error: Error? = null,
    val status: String
) : Parcelable

@Parcelize
data class Error(
    val expiredAt: String,
    val message: String,
    val name: String
) : Parcelable