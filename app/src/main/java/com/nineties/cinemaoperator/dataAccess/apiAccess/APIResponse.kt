package com.nineties.cinemaoperator.dataAccess.apiAccess

import retrofit2.Response

sealed class APIResponse<out T> {
    companion object {
        fun <T> create(response: Response<T>): APIResponse<T> {
            return if(response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    SuccessEmptyResponse()
                } else {
                    SuccessResponse(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMessage = if(msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ErrorResponse(errorMessage ?: "Unknown error")
            }
        }
    }
}

class SuccessResponse<T>(val data: T): APIResponse<T>()
class SuccessEmptyResponse<T>: APIResponse<T>()
class ErrorResponse<T>(val errorMessage: String): APIResponse<T>()
