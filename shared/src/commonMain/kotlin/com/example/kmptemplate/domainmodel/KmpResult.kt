package com.example.kmptemplate.domainmodel

sealed class KmpResult<T: Any> {
    data class Success<T: Any>(val value: T): KmpResult<T>()

    data class Failure<T: Any>(val error: KmpError): KmpResult<T>()
}

sealed interface KmpError {
    val msg: String

    data class UnAuthorizedError(override val msg: String): KmpError

    data class ClientError(override val msg: String): KmpError

    data class ServerError(override val msg: String): KmpError

    data class NetworkError(override val msg: String): KmpError
}