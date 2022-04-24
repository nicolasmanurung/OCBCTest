package com.test.ocbc.data.source.network

import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.request.RegistrationRequest
import com.test.ocbc.data.source.network.response.BalanceResponse
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.data.source.network.response.RegistrationResponse
import com.test.ocbc.data.source.network.response.TransactionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OCBCService {

    @POST("login")
    suspend fun userLogin(@Body body: LoginRequest): Response<LoginResponse>

    @GET("balance")
    suspend fun getUserBalance(
        @Header("Authorization") authToken: String
    ): Response<BalanceResponse>

    @GET("transactions")
    suspend fun getUserTransactions(
        @Header("Authorization") authToken: String
    ): Response<TransactionsResponse>

    @POST("register")
    suspend fun postUserRegistration(@Body body: RegistrationRequest): Response<RegistrationResponse>
}