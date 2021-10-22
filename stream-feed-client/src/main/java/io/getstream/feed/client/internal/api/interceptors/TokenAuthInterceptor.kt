package io.getstream.feed.client.internal.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An OkHttp interceptor which add the Authorization token needed for requesting to Stream Server. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [OkHttpClient.networkInterceptors].
 *
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
