package io.getstream.feed.client.internal.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An OkHttp [Interceptor] which add the SDK Version when requesting to Stream Server. Can be applied as an
 * Normal Interceptor or as a Network Interceptor.
 *
 * @property sdkVersion is the SDK Version of Stream Feed SDK.
 */
internal class SDKVersionInterceptor(private val sdkVersion: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return chain.proceed(
            original.newBuilder()
                .url(
                    original.url.newBuilder()
                        .removeAllQueryParameters(PARAM_SDK_VERSION_KEY)
                        .addQueryParameter(PARAM_SDK_VERSION_KEY, sdkVersion)
                        .build()
                )
                .build()
        )
    }

    companion object {
        private const val PARAM_SDK_VERSION_KEY = "X-Stream-Client"
    }
}
