package com.pal.rickandmorty.data.network.model

import com.pal.rickandmorty.domain.AppFailure

sealed class ApiFailure : Throwable() {
    sealed class ClientError : ApiFailure() {
        class BadRequest : ClientError() // 400
        class Unauthorized : ClientError() // 401
        class Forbidden :
            ClientError() // 403 Unlike 401 Unauthorized, the client's identity is known to the server.

        class NotFound : ClientError() // 404
        class Timeout : ClientError() // 408

        class UnknownClientError : ClientError()
    }

    sealed class ServerError : ApiFailure() {
        class InternalServerError : ServerError() // 500
        class UnknownServerError : ServerError()
    }

    sealed class NetworkError : ApiFailure() {
        class NoInternet : NetworkError()
    }

    class UnknownError : ApiFailure()
}

fun ApiFailure.toDomainFailure(): AppFailure {
    return when (this) {
        is ApiFailure.ClientError.BadRequest,
        is ApiFailure.ClientError.Unauthorized,
        is ApiFailure.ClientError.Forbidden,
        is ApiFailure.ClientError.NotFound,
        is ApiFailure.ClientError.Timeout,
        is ApiFailure.ClientError.UnknownClientError -> AppFailure.RemoteAppFailure.RequestError()

        is ApiFailure.ServerError.InternalServerError,
        is ApiFailure.ServerError.UnknownServerError -> AppFailure.RemoteAppFailure.ServerError()

        is ApiFailure.NetworkError.NoInternet -> AppFailure.RemoteAppFailure.ConnectivityError()
        is ApiFailure.UnknownError -> AppFailure.UnknownError()
    }
}