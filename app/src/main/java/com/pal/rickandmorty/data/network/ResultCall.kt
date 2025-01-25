package com.pal.rickandmorty.data.network

import com.pal.rickandmorty.data.network.model.ApiFailure
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.NoRouteToHostException
import java.net.UnknownHostException


class ResultCall<T>(private val delegate: Call<T>) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) =
                    if (response.isSuccessful) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(
                                response.code(),
                                Result.success(response.body()!!)
                            )
                        )
                    } else {
                        val apiFailure = when(response.code()) {
                            400 -> ApiFailure.ClientError.BadRequest()
                            401 -> ApiFailure.ClientError.Unauthorized()
                            403 -> ApiFailure.ClientError.Forbidden()
                            404 -> ApiFailure.ClientError.NotFound()
                            408 -> ApiFailure.ClientError.Timeout()
                            in 400..499 -> ApiFailure.ClientError.UnknownClientError()
                            500 -> ApiFailure.ServerError.InternalServerError()
                            in 501 .. 511 -> ApiFailure.ServerError.UnknownServerError()
                            else -> ApiFailure.UnknownError()
                        }
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(apiFailure))
                        )

                    }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    val apiFailure = when (t) {
                        is UnknownHostException -> ApiFailure.NetworkError.NoInternet()
                        is NoRouteToHostException -> ApiFailure.NetworkError.NoInternet()
                        else -> ApiFailure.UnknownError()
                    }
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.failure(apiFailure))
                    )
                }
            }
        )
    }

    override fun isExecuted() = delegate.isExecuted

    override fun execute(): Response<Result<T>> =
        Response.success(Result.success(delegate.execute().body()!!))

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
