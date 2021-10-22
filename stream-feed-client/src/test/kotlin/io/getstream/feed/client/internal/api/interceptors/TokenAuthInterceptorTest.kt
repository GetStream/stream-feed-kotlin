package io.getstream.feed.client.internal.api.interceptors

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.getstream.feed.client.Mother
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class TokenAuthInterceptorTest {

    /** [arguments] */
    @ParameterizedTest
    @MethodSource("arguments")
    fun test(originalHeaders: Headers, getToken: () -> String, expectedHeaders: Headers) {
        val tokenAuthInterceptor = TokenAuthInterceptor(getToken)
        val chain: Interceptor.Chain = mock()
        val response: Response = mock()
        whenever(chain.request()) doReturn Request.Builder().url("https://blablabal").headers(originalHeaders).build()
        whenever(chain.proceed(any())) doReturn response

        val responseResult = tokenAuthInterceptor.intercept(chain)

        verify(chain).proceed(
            com.nhaarman.mockitokotlin2.check { result ->
                result.headers `should be equal to` expectedHeaders
            }
        )
        responseResult `should be equal to` response
    }

    companion object {

        @JvmStatic
        fun arguments() = Mother.randomString().let { token ->
            listOf(
                arguments(token),
                arguments(token, Mother.randomString() to Mother.randomString()),
                arguments(token, "Stream-Auth-Type" to "jwt"),
                arguments(token, Mother.randomString() to Mother.randomString(), "Stream-Auth-Type" to "jwt"),
                arguments(token, "Stream-Auth-Type" to Mother.randomString()),
                arguments(token, Mother.randomString() to Mother.randomString(), "Stream-Auth-Type" to Mother.randomString()),
                arguments(token, "Authorization" to token),
                arguments(token, Mother.randomString() to Mother.randomString(), "Authorization" to token),
                arguments(token, "Authorization" to Mother.randomString()),
                arguments(token, Mother.randomString() to Mother.randomString(), "Authorization" to Mother.randomString()),
                arguments(token, "Stream-Auth-Type" to "jwt", "Authorization" to Mother.randomString()),
                arguments(token, Mother.randomString() to Mother.randomString(), "Stream-Auth-Type" to "jwt", "Authorization" to Mother.randomString()),
                arguments(token, "Stream-Auth-Type" to Mother.randomString(), "Authorization" to token),
                arguments(token, Mother.randomString() to Mother.randomString(), "Stream-Auth-Type" to Mother.randomString(), "Authorization" to token),
                arguments(token, "Stream-Auth-Type" to Mother.randomString(), "Authorization" to Mother.randomString()),
                arguments(token, Mother.randomString() to Mother.randomString(), "Stream-Auth-Type" to Mother.randomString(), "Authorization" to Mother.randomString()),
            )
        }

        private fun arguments(token: String, vararg headerValues: Pair<String, String>): Arguments {
            return Arguments.of(
                headers(*headerValues),
                { token },
                headersWithToken(token, *headerValues)
            )
        }

        private fun headersWithToken(token: String, vararg headerValues: Pair<String, String>): Headers =
            headers(*headerValues)
                .newBuilder()
                .set("Stream-Auth-Type", "jwt")
                .set("Authorization", token)
                .build()

        private fun headers(vararg headerValues: Pair<String, String>): Headers =
            Headers.Builder().apply {
                headerValues.forEach {
                    add(it.first, it.second)
                }
            }
                .build()
    }
}
