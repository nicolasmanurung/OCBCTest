package com.test.ocbc.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.ocbc.base.BaseActivityBinding
import com.test.ocbc.data.source.prefs.UserData
import com.test.ocbc.databinding.ActivityDashboardBinding
import com.test.ocbc.ui.MainNavigatorActivity
import com.test.ocbc.ui.OCBCViewModel
import com.test.ocbc.ui.dashboard.adapter.TransactionHistoryAdapter
import com.test.ocbc.utils.CurrencyUtil.toDollar
import com.test.ocbc.utils.ViewUtil.showSnackImageToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DashboardActivity : BaseActivityBinding<ActivityDashboardBinding>() {
    private val viewModel: OCBCViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionHistoryAdapter

    override val bindingInflater: (LayoutInflater) -> ActivityDashboardBinding =
        ActivityDashboardBinding::inflate

    override fun setupView(binding: ActivityDashboardBinding) {
        checkIsLoginValid()
        observeUserData()
        observeUserBalance()
        observeListTransactionsHistory()
        setupAdapter()

        // Fetch Data Trigger Here
        getAllData()

        binding.btnLogout.setOnClickListener {
            userLogout()
        }
    }

    private fun getAllData() = CoroutineScope(Dispatchers.IO).launch {
        val userData = prefs.userData.first()
        val authToken = prefs.authToken.first()

        // Fetch Data Trigger Here
        viewModel.getUserTransactions(authToken = authToken)
        viewModel.getUserBalance(authToken = authToken, userData = userData)
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
        viewModel.userBalance.observe(this) { response ->
            if (response != null) {
                binding.txtAmount.text = "SGD " + response.balance?.toDollar()
            }
        }

        viewModel.userBalanceThrowable.observe(this) { response ->
            when (response) {
                401 -> {
                    showSnackImageToast("Auth has expired, Please Re-log in")
                    userLogout()
                }
            }
        }
    }

    private fun observeListTransactionsHistory() {
        viewModel.userTransactions.observe(this) { response ->
            if (response?.isNullOrEmpty() == false) {
                transactionAdapter.differ.submitList(response)
            }
        }

        viewModel.userTransactionsThrowable.observe(this) { response ->
            when (response) {
                401 -> {
                    showSnackImageToast("Auth has expired, Please Re-log in")
                    userLogout()
                }
            }
        }
    }

    private fun observeUserData() {
        viewModel.userData.observe(this) { response ->
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
        checkIsLoginValid()
    }

    private fun checkIsLoginValid() = CoroutineScope(Dispatchers.Main).launch {
        val isUserLogin = prefs.isUserLogin.firstOrNull()
        if (isUserLogin == false) {
            startActivity(Intent(this@DashboardActivity, MainNavigatorActivity::class.java))
            finish()
        }
    }
}