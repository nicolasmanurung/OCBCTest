package com.test.ocbc.data.source.network

import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OCBCService {

    @POST("login")
    suspend fun userLogin(@Body body: LoginRequest): Response<LoginResponse>
}