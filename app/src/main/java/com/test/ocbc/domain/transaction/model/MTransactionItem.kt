package com.test.ocbc.domain.transaction.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MTransactionItem(
    val groupDate: String,
    val listTransactionDetail: List<MDetailTransaction>
) : Parcelable

@Parcelize
data class MDetailTransaction(
    val amount: Double,
    val description: String? = "",
    val transactionId: String,
    val transactionType: String,
    val accountHolder: String,
    val accountNo: String,
    val transactionDate: String
) : Parcelable
