package io.getstream.feed.client.internal.api.interceptors

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.getstream.feed.client.Mother
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class SDKVersionInterceptorTest {

    /** [arguments] */
    @ParameterizedTest
    @MethodSource("arguments")
    fun test(url: HttpUrl, apiKey: String, expectedUrl: HttpUrl) {
        val sdkVersionInterceptor = SDKVersionInterceptor(apiKey)
        val chain: Interceptor.Chain = mock()
        val response: Response = mock()
        whenever(chain.request()) doReturn Request.Builder().url(url).build()
        whenever(chain.proceed(any())) doReturn response

        val responseResult = sdkVersionInterceptor.intercept(chain)

        verify(chain).proceed(
            com.nhaarman.mockitokotlin2.check { result ->
                result.url `should be equal to` expectedUrl
            }
        )
        responseResult `should be equal to` response
    }

    companion object {

        @JvmStatic
        fun arguments() = Mother.randomString().let { domain ->
            Mother.randomString().let { sdkVersion ->
                listOf(
                    Arguments.of("http://$domain".toHttpUrl(), sdkVersion, "http://$domain?X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("https://$domain".toHttpUrl(), sdkVersion, "https://$domain?X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("http://$domain?X-Stream-Client=$sdkVersion".toHttpUrl(), sdkVersion, "http://$domain?X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("https://$domain?X-Stream-Client=$sdkVersion".toHttpUrl(), sdkVersion, "https://$domain?X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("http://$domain?X-Stream-Client=${Mother.randomString()}".toHttpUrl(), sdkVersion, "http://$domain?X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("https://$domain?X-Stream-Client=${Mother.randomString()}".toHttpUrl(), sdkVersion, "https://$domain?X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("http://$domain?other_query_param=$sdkVersion".toHttpUrl(), sdkVersion, "http://$domain?other_query_param=$sdkVersion&X-Stream-Client=$sdkVersion".toHttpUrl()),
                    Arguments.of("https://$domain?other_query_param=$sdkVersion".toHttpUrl(), sdkVersion, "https://$domain?other_query_param=$sdkVersion&X-Stream-Client=$sdkVersion".toHttpUrl()),
                )
            }
        }
    }
}
