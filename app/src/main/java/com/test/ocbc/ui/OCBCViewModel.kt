package com.test.ocbc.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.ocbc.data.source.OCBCRepository
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OCBCViewModel @Inject constructor(
    private val repository: OCBCRepository
) : ViewModel() {

    private val _loginAuth = MutableLiveData<LoginResponse?>()
    val loginAuth: LiveData<LoginResponse?>
        get() = _loginAuth
    private val _loginAuthThrowable = MutableLiveData<Int>()
    val loginAuthThrowable: LiveData<Int?>
        get() = _loginAuthThrowable

    fun login(body: LoginRequest) = viewModelScope.launch {
        repository.postUserLogin(body).collect { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let {

                        _loginAuth.postValue(it)
                    }
                }

                is NetworkResult.Error -> {
                    _loginAuth.postValue(null)

                    result.code?.let {
                        _loginAuthThrowable.postValue(it)
                    }
                }
            }
        }
    }

}