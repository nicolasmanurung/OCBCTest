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
    val receipient: Receipient? = null,
    val sender: Sender? = null,
    val transactionDate: String,
    val transactionId: String,
    val transactionType: String
) : Parcelable

@Parcelize
data class Receipient(
    val accountHolder: String = "Unknown",
    val accountNo: String
) : Parcelable

@Parcelize
data class Sender(
    val accountHolder: String = "Unknown",
    val accountNo: String
) : Parcelable