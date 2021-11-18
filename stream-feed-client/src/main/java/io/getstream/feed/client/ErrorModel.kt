package io.getstream.feed.client

sealed class StreamError
data class StreamAPIError(
    val detail: String,
    val statusCode: Int,
    val errorCode: Int,
    val error: String,
    val moreInfo: String,
) : StreamError()
data class NetworkError(val message: String) : StreamError()
sealed class ParamError : StreamError() {
    abstract val message: String
}
data class NegativeParamError(override val message: String) : ParamError()
data class IncompatibleParamsError(override val message: String) : ParamError()
data class EmptyParamError(override val message: String) : ParamError()
data class InvalidParamError(override val message: String) : ParamError()
