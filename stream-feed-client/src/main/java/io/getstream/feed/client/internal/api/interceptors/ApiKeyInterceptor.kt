package io.getstream.feed.client.internal.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An OkHttp interceptor which add the API Key needed for requesting to Stream Server. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [OkHttpClient.networkInterceptors].
 *
 */
internal class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return chain.proceed(
            original.newBuilder()
                .url(
                    original.url.newBuilder()
                        .removeAllQueryParameters(PARAM_API_KEY)
                        .addQueryParameter(PARAM_API_KEY, apiKey)
                        .build()
                )
                .build()
        )
    }

    companion object {
        private const val PARAM_API_KEY = "api_key"
    }
}
