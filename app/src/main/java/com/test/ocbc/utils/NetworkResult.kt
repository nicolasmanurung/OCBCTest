package com.test.ocbc.utils

import retrofit2.Response
import timber.log.Timber

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(data: T? = null, message: String, code: Int) :
        NetworkResult<T>(data, message, code)

    companion object {
        suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
            return try {
                val response = apiCall()
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    return Success(body)
                }
                Timber.e("request failed:\t $response")
                error("${response.code()} ${response.message()}", response.code())
            } catch (e: Exception) {
                Timber.e(e, "request failed")
                error(e.message ?: e.toString(), 0)
            }
        }

        private fun <T> error(errorMessage: String, code: Int): NetworkResult<T> =
            Error(null, "Api call failed $errorMessage", code = code)
    }
}
