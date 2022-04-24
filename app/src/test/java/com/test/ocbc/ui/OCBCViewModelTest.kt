package com.test.ocbc.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.test.ocbc.data.source.OCBCRepository
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.data.source.prefs.UserPreferences
import com.test.ocbc.utils.CurrencyUtil.toDollar
import com.test.ocbc.utils.DateUtil.changeTo
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

    @Test
    fun testDateTrue() = runTest {
        assertThat("2022-03-10T15:36:52.413Z".changeTo("dd MMM yyyy")).isEqualTo("10 Mar 2022")
    }

    @Test
    fun testMoneyTrue() = runTest {
        assertThat(5200.20.toDollar()).isEqualTo("5,200.20")
    }
}