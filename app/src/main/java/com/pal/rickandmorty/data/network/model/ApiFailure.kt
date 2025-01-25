package com.pal.rickandmorty.data.network.model

sealed class ApiFailure : Throwable() {
    sealed class ClientError : ApiFailure() {
        class BadRequest : ClientError() // 400
        class Unauthorized : ClientError() // 401
        class Forbidden : ClientError() // 403 Unlike 401 Unauthorized, the client's identity is known to the server.

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