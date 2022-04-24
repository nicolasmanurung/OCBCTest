package com.test.ocbc.data.source.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionsResponse(
    val `data`: List<TransactionItem>? = listOf(),
    val status: String? = "",
    val error: Error? = null,
) : Parcelable

@Parcelize
data class TransactionItem(
    val amount: Double,
    val description: String = "",
    val receipient: Receipient,
    val transactionDate: String,
    val transactionId: String,
    val transactionType: String
) : Parcelable

@Parcelize
data class Receipient(
    val accountHolder: String,
    val accountNo: String
) : Parcelable