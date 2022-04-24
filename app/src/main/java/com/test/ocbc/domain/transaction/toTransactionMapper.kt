package com.test.ocbc.domain.transaction

import com.test.ocbc.data.source.network.response.TransactionItem
import com.test.ocbc.domain.transaction.model.MDetailTransaction
import com.test.ocbc.domain.transaction.model.MTransactionItem
import com.test.ocbc.utils.DateUtil.changeTo

fun toTransactionMapper(response: List<TransactionItem>): List<MTransactionItem> {
    val data = response.groupBy { it.transactionDate.changeTo("dd MMM yyyy") }

    return data.map {
        val listDetail = arrayListOf<MDetailTransaction>()
        it.value.forEach { item ->
            var accountHolder = ""
            var accountNo = ""
            when (item.transactionType) {
                "received" -> {
                    item.sender?.accountHolder?.let { acHolder -> accountHolder = acHolder }
                    item.sender?.accountNo?.let { acNo -> accountNo = acNo }
                }
                "transfer" -> {
                    item.receipient?.accountHolder?.let { acHolder -> accountHolder = acHolder }
                    item.receipient?.accountNo?.let { acNo -> accountNo = acNo }
                }
            }
            listDetail.add(
                MDetailTransaction(
                    transactionDate = item.transactionDate,
                    amount = item.amount,
                    description = item.description,
                    transactionId = item.transactionId,
                    transactionType = item.transactionType,
                    accountHolder = accountHolder,
                    accountNo = accountNo
                )
            )
        }
        MTransactionItem(
            groupDate = it.key,
            listTransactionDetail = listDetail
        )
    }
}