package com.test.ocbc.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.test.ocbc.data.source.OCBCRepository
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.BalanceResponse
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.data.source.network.response.Receipient
import com.test.ocbc.data.source.network.response.TransactionItem
import com.test.ocbc.data.source.network.response.TransactionsResponse
import com.test.ocbc.data.source.prefs.UserPreferences
import com.test.ocbc.utils.CurrencyUtil.toDollar
import com.test.ocbc.utils.DateUtil.changeTo
import com.test.ocbc.utils.NetworkResult
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
    private val transactionItemDataTest = listOf(
        TransactionItem(
            amount = 50.0,
            receipient = Receipient(accountHolder = "Nicolas", accountNo = "1234-123-1234"),
            transactionDate = "2022-03-10T15:36:52.413Z",
            transactionId = "622cb8b68c7f3a5bcccf8ba4",
            transactionType = "transfer"
        )
    )
    private val transactionResponse =
        TransactionsResponse(status = "success", data = transactionItemDataTest)

    private val balanceResponse = BalanceResponse(accountNo = "Testing")

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

    @Test
    fun testGetUserBalanceSuccess() = runTest {
        val sut = OCBCViewModel(repository, userPreferences)
        whenever(repository.getUserBalance("TESTING_AUTH")).thenReturn(
            flowOf(
                NetworkResult.Success(balanceResponse)
            )
        )

        sut.getUserBalance("TESTING_AUTH", null)
        val dataResponse = sut.userBalance.value

        verify(repository).getUserBalance("TESTING_AUTH")
        assertThat(dataResponse?.accountNo).isEqualTo("Testing")
    }

    @Test
    fun testGetUserBalanceThrowable() = runTest {
        val sut = OCBCViewModel(repository, userPreferences)
        whenever(repository.getUserBalance("TESTING_AUTH"))
            .thenReturn(flowOf(NetworkResult.Error(message = "Not Found", code = 404)))

        sut.getUserBalance("TESTING_AUTH", null)
        val resultError = sut.userBalanceThrowable.value

        verify(repository).getUserBalance("TESTING_AUTH")
        assertThat(resultError).isEqualTo(404)
    }

    @Test
    fun testGetUserTransactionSuccess() = runTest {
        val sut = OCBCViewModel(repository, userPreferences)
        whenever(repository.getUserTransactions("TESTING_AUTH"))
            .thenReturn(flowOf(NetworkResult.Success(transactionResponse)))

        sut.getUserTransactions("TESTING_AUTH")
        val dataResponse = sut.userTransactions.value

        verify(repository).getUserTransactions("TESTING_AUTH")
        assertThat(dataResponse?.size).isEqualTo(1)
        assertThat(dataResponse?.first()?.listTransactionDetail?.first()?.transactionId).isEqualTo("622cb8b68c7f3a5bcccf8ba4")
    }

    @Test
    fun testGetUserTransactionThrowable() = runTest {
        val sut = OCBCViewModel(repository, userPreferences)
        whenever(repository.getUserTransactions("TESTING_AUTH"))
            .thenReturn(flowOf(NetworkResult.Error(message = "Not Found", code = 404)))

        sut.getUserTransactions("TESTING_AUTH")
        val resultError = sut.userTransactionsThrowable.value

        verify(repository).getUserTransactions("TESTING_AUTH")
        assertThat(resultError).isEqualTo(404)
    }
}