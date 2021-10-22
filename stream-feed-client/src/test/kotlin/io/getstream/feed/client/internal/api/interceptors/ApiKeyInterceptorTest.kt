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

internal class ApiKeyInterceptorTest {

    /** [arguments] */
    @ParameterizedTest
    @MethodSource("arguments")
    fun test(url: HttpUrl, apiKey: String, expectedUrl: HttpUrl) {
        val apiKeyInterceptor = ApiKeyInterceptor(apiKey)
        val chain: Interceptor.Chain = mock()
        val response: Response = mock()
        whenever(chain.request()) doReturn Request.Builder().url(url).build()
        whenever(chain.proceed(any())) doReturn response

        val responseResult = apiKeyInterceptor.intercept(chain)

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
            Mother.randomString().let { apiKey ->
                listOf(
                    Arguments.of("http://$domain".toHttpUrl(), apiKey, "http://$domain?api_key=$apiKey".toHttpUrl()),
                    Arguments.of("https://$domain".toHttpUrl(), apiKey, "https://$domain?api_key=$apiKey".toHttpUrl()),
                    Arguments.of("http://$domain?api_key=$apiKey".toHttpUrl(), apiKey, "http://$domain?api_key=$apiKey".toHttpUrl()),
                    Arguments.of("https://$domain?api_key=$apiKey".toHttpUrl(), apiKey, "https://$domain?api_key=$apiKey".toHttpUrl()),
                    Arguments.of("http://$domain?api_key=${Mother.randomString()}".toHttpUrl(), apiKey, "http://$domain?api_key=$apiKey".toHttpUrl()),
                    Arguments.of("https://$domain?api_key=${Mother.randomString()}".toHttpUrl(), apiKey, "https://$domain?api_key=$apiKey".toHttpUrl()),
                    Arguments.of("http://$domain?other_query_param=$apiKey".toHttpUrl(), apiKey, "http://$domain?other_query_param=$apiKey&api_key=$apiKey".toHttpUrl()),
                    Arguments.of("https://$domain?other_query_param=$apiKey".toHttpUrl(), apiKey, "https://$domain?other_query_param=$apiKey&api_key=$apiKey".toHttpUrl()),
                )
            }
        }
    }
}
