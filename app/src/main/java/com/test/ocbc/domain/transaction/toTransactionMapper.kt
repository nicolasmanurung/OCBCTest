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
            listDetail.add(
                MDetailTransaction(
                    transactionDate = item.transactionDate,
                    amount = item.amount,
                    description = item.description,
                    transactionId = item.transactionId,
                    transactionType = item.transactionType,
                    accountHolder = item.receipient.accountHolder,
                    accountNo = item.receipient.accountNo
                )
            )
        }
        MTransactionItem(
            groupDate = it.key,
            listTransactionDetail = listDetail
        )
    }
}