package com.test.ocbc.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.ocbc.base.BaseFragmentBinding
import com.test.ocbc.data.source.prefs.UserData
import com.test.ocbc.databinding.FragmentDashboardBinding
import com.test.ocbc.ui.OCBCViewModel
import com.test.ocbc.ui.dashboard.adapter.TransactionHistoryAdapter
import com.test.ocbc.utils.CurrencyUtil.toDollar
import com.test.ocbc.utils.ViewUtil.showSnackImageToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class DashboardFragment : BaseFragmentBinding<FragmentDashboardBinding>() {
    private val viewModel: OCBCViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionHistoryAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding =
        FragmentDashboardBinding::inflate

    override fun setupView(binding: FragmentDashboardBinding) {
        observeUserData()
        observeUserBalance()
        observeListTransactionsHistory()
        setupAdapter()

        // Fetch Data Trigger Here
        viewModel.getUserTransactions()
        viewModel.getUserBalance()

        binding.btnLogout.setOnClickListener {
            userLogout()
        }
    }

    private fun setupAdapter() {
        transactionAdapter = TransactionHistoryAdapter()
        binding.rvTransactionsHistory.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = transactionAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUserBalance() {
        viewModel.userBalance.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                binding.txtAmount.text = "SGD " + response.balance?.toDollar()
            }
        }

        viewModel.userBalanceThrowable.observe(viewLifecycleOwner) { response ->
            when (response) {
                401 -> {
                    requireActivity().showSnackImageToast("Auth has expired, Please Re-log in")
                    userLogout()
                }
            }
        }
    }

    private fun observeListTransactionsHistory() {
        viewModel.userTransactions.observe(viewLifecycleOwner) { response ->
            if (response?.isNullOrEmpty() == false) {
                transactionAdapter.differ.submitList(response)
            }
        }

        viewModel.userTransactionsThrowable.observe(viewLifecycleOwner) { response ->
            when (response) {
                401 -> {
                    requireActivity().showSnackImageToast("Auth has expired, Please Re-log in")
                    userLogout()
                }
            }
        }
    }

    private fun observeUserData() {
        viewModel.userData.observe(viewLifecycleOwner) { response ->
            response?.let { dataUser ->
                dataUser.accountNo?.let {
                    binding.txtAccountNo.text = it
                }
                dataUser.username?.let {
                    binding.txtAccountHolderName.text = it
                }
            }
        }
    }

    private fun userLogout() = runBlocking(Dispatchers.IO) {
        prefs.saveToken("")
        prefs.saveUserData(UserData("-", "-"))
        prefs.isUserLogin(false)
    }
}