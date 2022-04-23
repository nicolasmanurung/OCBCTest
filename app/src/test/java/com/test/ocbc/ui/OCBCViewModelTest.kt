package com.test.ocbc.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.test.ocbc.data.source.OCBCRepository
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.data.source.prefs.UserPreferences
import com.test.ocbc.utils.NetworkResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class OCBCViewModelTest {
    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository = mock<OCBCRepository>()
    private val userPreferences = mock<UserPreferences>()
    private val loginResponse = LoginResponse()

    private lateinit var viewModel: OCBCViewModel

    @Before
    fun setUp() {
        viewModel = OCBCViewModel(repository, userPreferences)
    }

    @Test
    fun testGetSuccessLoginAuth() = runTest {
        val sut = OCBCViewModel(repository, userPreferences)
        val bodyRequest = LoginRequest(
            "testing",
            "testing"
        )
        whenever(repository.postUserLogin(eq(bodyRequest))).thenReturn(
            flowOf(NetworkResult.Success(loginResponse))
        )
        sut.login(bodyRequest)
        val dataResponse = sut.loginAuth.value

        delay(30000)

        verify(repository).postUserLogin(eq(bodyRequest))
        assertThat(dataResponse?.accountNo).isEqualTo("")
        assertThat(dataResponse?.username).isEqualTo("")
    }

    @Test
    fun testGetLoginAuthThrowable() = runTest {
        val sut = OCBCViewModel(repository, userPreferences)
        val bodyRequest = LoginRequest(
            "testing",
            "testing"
        )
        whenever(repository.postUserLogin(eq(bodyRequest)))
            .thenReturn(flowOf(NetworkResult.Error(message = "Not Found", code = 404)))

        sut.login(bodyRequest)
        val resultError = sut.loginAuthThrowable.value

        verify(repository).postUserLogin(eq(bodyRequest))
        assertThat(resultError).isEqualTo(404)
    }
}