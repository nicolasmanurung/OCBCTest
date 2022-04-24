package com.test.ocbc.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.ocbc.data.source.OCBCRepository
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.BalanceResponse
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.data.source.prefs.UserData
import com.test.ocbc.data.source.prefs.UserPreferences
import com.test.ocbc.domain.transaction.model.MTransactionItem
import com.test.ocbc.domain.transaction.toTransactionMapper
import com.test.ocbc.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OCBCViewModel @Inject constructor(
    private val repository: OCBCRepository,
    private val userPref: UserPreferences
) : ViewModel() {

    private val _loginAuth = MutableLiveData<LoginResponse?>()
    val loginAuth: LiveData<LoginResponse?>
        get() = _loginAuth
    private val _loginAuthThrowable = MutableLiveData<Int>()
    val loginAuthThrowable: LiveData<Int?>
        get() = _loginAuthThrowable

    private val _userBalance = MutableLiveData<BalanceResponse?>()
    val userBalance: LiveData<BalanceResponse?>
        get() = _userBalance
    private val _userBalanceThrowable = MutableLiveData<Int?>()
    val userBalanceThrowable: LiveData<Int?>
        get() = _userBalanceThrowable

    private val _userTransactions = MutableLiveData<List<MTransactionItem>?>()
    val userTransactions: LiveData<List<MTransactionItem>?>
        get() = _userTransactions
    private val _userTransactionsThrowable = MutableLiveData<Int?>()
    val userTransactionsThrowable: LiveData<Int?>
        get() = _userTransactionsThrowable

    private val _showingLoading = MutableLiveData(false)
    val showingLoading: LiveData<Boolean>
        get() = _showingLoading

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?>
        get() = _userData

    fun login(body: LoginRequest) = viewModelScope.launch {
        _showingLoading.postValue(true)
        repository.postUserLogin(body).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {
                        _showingLoading.postValue(false)
                        _loginAuth.postValue(it)
                        runBlocking {
                            val userData = UserData(
                                username = it.username.toString(),
                                accountNo = it.accountNo.toString()
                            )
                            userPref.saveUserData(userData)
                            userPref.saveToken(it.token.toString())
                            userPref.isUserLogin(true)
                        }
                    }
                }

                is NetworkResult.Error -> {
                    _showingLoading.postValue(false)
                    _loginAuth.postValue(null)

                    result.code?.let {
                        _loginAuthThrowable.postValue(it)
                    }
                }
            }
        }
    }

    fun getUserBalance(authToken: String?, userData: UserData?) = viewModelScope.launch {
        _userData.postValue(userData)
        authToken?.let {
            repository.getUserBalance(it).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data?.let {
                            _userBalance.postValue(it)
                        }
                    }

                    is NetworkResult.Error -> {
                        _userBalance.postValue(null)
                        result.code?.let {
                            _userBalanceThrowable.postValue(it)
                        }
                    }
                }
            }
        }
    }

    fun getUserTransactions(authToken: String?) = viewModelScope.launch {
        authToken?.let{
            repository.getUserTransactions(it).collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data?.let {
                            _userTransactions.postValue(it.data?.let { transactionList ->
                                toTransactionMapper(
                                    transactionList
                                )
                            })
                        }
                    }

                    is NetworkResult.Error -> {
                        _userTransactions.postValue(null)
                        result.code?.let {
                            _userTransactionsThrowable.postValue(it)
                        }
                    }
                }
            }
        }
    }
}