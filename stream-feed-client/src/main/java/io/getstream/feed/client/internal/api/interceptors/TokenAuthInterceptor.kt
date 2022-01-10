package io.getstream.feed.client.internal.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An OkHttp [Interceptor] which adds the Authorization token needed for requesting to Stream Server. Can be applied as an
 * Normal Interceptor or as a Network Interceptor.
 *
 * @property getToken is a function that will be called everytime a token needs to be injected on a request.
 * This function return and string with the the token to be used.
 */
internal class TokenAuthInterceptor(
    val getToken: () -> String
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain
                .request()
                .newBuilder()
                .header("Stream-Auth-Type", "jwt")
                .header("Authorization", getToken())
                .build()
        )
    }
}
