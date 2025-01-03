package com.example.kmptemplate.domainmodel

sealed class KmpResult<T : Any> {
    data class Success<T : Any>(val value: T) : KmpResult<T>()

    data class Failure<T : Any>(val error: KmpError) : KmpResult<T>()
}

sealed interface KmpError {
    val msg: String

    /**
     * 引数が適切でないことが原因でクライアント側で発生するエラー
     */
    data class IllegalArgumentError(override val msg: String) : KmpError

    data class UnAuthorizedError(override val msg: String) : KmpError

    data class ClientError(override val msg: String) : KmpError

    data class ServerError(override val msg: String) : KmpError

    data class NetworkError(override val msg: String) : KmpError
}

fun <T : Any, U : Any> KmpResult<T>.convertType(converter: (T) -> U): KmpResult<U> {
    return when (this) {
        is KmpResult.Success -> {
            KmpResult.Success(converter(this.value))
        }
        is KmpResult.Failure -> {
            KmpResult.Failure(this.error)
        }
    }
}

/**
 * もし成功していたら次の処理を行う
 * 失敗していなた何もせず発生していたエラーを返す
 */
suspend fun <T : Any, U : Any> KmpResult<T>.chain(process: suspend (T) -> KmpResult<U>): KmpResult<U> {
    return when (this) {
        is KmpResult.Success -> {
            process(this.value)
        }
        is KmpResult.Failure -> {
            KmpResult.Failure(this.error)
        }
    }
}
