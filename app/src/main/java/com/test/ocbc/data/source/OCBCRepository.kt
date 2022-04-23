package com.test.ocbc.data.source

import com.test.ocbc.data.source.network.OCBCService
import com.test.ocbc.data.source.network.request.LoginRequest
import com.test.ocbc.data.source.network.response.LoginResponse
import com.test.ocbc.di.IoDispatcher
import com.test.ocbc.utils.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OCBCRepository @Inject constructor(
    private val service: OCBCService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun postUserLogin(body: LoginRequest): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(NetworkResult.safeApiCall {
                service.userLogin(body)
            })
        }.flowOn(dispatcher)
    }
}