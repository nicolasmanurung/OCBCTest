package com.test.ocbc.ui.dashboard.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.ocbc.R
import com.test.ocbc.databinding.ItemCardTransactionHistoryBinding
import com.test.ocbc.databinding.ItemTransactionHistoryBinding
import com.test.ocbc.domain.transaction.model.MDetailTransaction
import com.test.ocbc.domain.transaction.model.MTransactionItem
import com.test.ocbc.utils.CurrencyUtil.toDollar

class TransactionHistoryAdapter :
    RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder>() {

    inner class TransactionHistoryViewHolder(private val binding: ItemCardTransactionHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MTransactionItem) {
            val adapterItem = TransactionItemAdapter()
            adapterItem.differ.submitList(item.listTransactionDetail)
            binding.txtTransactionDate.text = item.groupDate
            binding.rvItemDetailTransaction.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = adapterItem
            }
        }
    }

    private var differCallback = object : DiffUtil.ItemCallback<MTransactionItem>() {
        override fun areItemsTheSame(
            oldItem: MTransactionItem,
            newItem: MTransactionItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: MTransactionItem,
            newItem: MTransactionItem
        ): Boolean = oldItem.groupDate == newItem.groupDate
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionHistoryViewHolder = TransactionHistoryViewHolder(
        ItemCardTransactionHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: TransactionHistoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class TransactionItemAdapter :
        RecyclerView.Adapter<TransactionItemAdapter.TransactionItemViewHolder>() {
        inner class TransactionItemViewHolder(private val binding: ItemTransactionHistoryBinding) :
            RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(item: MDetailTransaction) {
                binding.txtTransactionNameStakeholder.text = item.accountHolder
                binding.txtAccountNo.text = item.accountNo
                when (item.transactionType) {
                    "transfer" -> {
                        binding.txtSumTransaction.text = item.amount.toDollar()
                    }
                    else -> {
                        binding.txtSumTransaction.text = "-" + item.amount.toDollar()
                        binding.txtSumTransaction.setTextColor(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.primaryTextColor50
                            )
                        )
                    }
                }
            }
        }

        private var differCallback = object : DiffUtil.ItemCallback<MDetailTransaction>() {
            override fun areItemsTheSame(
                oldItem: MDetailTransaction,
                newItem: MDetailTransaction
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MDetailTransaction,
                newItem: MDetailTransaction
            ): Boolean = oldItem.transactionId == newItem.transactionId
        }

        val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): TransactionItemViewHolder = TransactionItemViewHolder(
            ItemTransactionHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
            holder.bind(differ.currentList[position])
        }

        override fun getItemCount(): Int = differ.currentList.size
    }
}