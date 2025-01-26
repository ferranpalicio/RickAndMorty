package com.pal.rickandmorty.domain

@SuppressWarnings("unused")
sealed class AppFailure : Throwable() {
    sealed class LocalAppFailure : AppFailure() {
        class NoDataError : LocalAppFailure()
    }

    sealed class RemoteAppFailure : AppFailure (){
        class RequestError : RemoteAppFailure()
        class ServerError : RemoteAppFailure()
        class ConnectivityError : RemoteAppFailure()
    }

    sealed class PermissionFailure : AppFailure() {
        class PermissionNotGrantedError : PermissionFailure()
    }

    class UnknownError : AppFailure()
}