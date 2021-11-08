package io.getstream.feed.client.internal.api.adapters

import io.getstream.feed.client.JSONMother.toJsonString
import io.getstream.feed.client.Mother.randomActivitiesRequest
import io.getstream.feed.client.Mother.randomUpdateActivitiesRequest
import io.getstream.feed.client.Mother.randomUpdateActivityByForeignIdRequest
import io.getstream.feed.client.Mother.randomUpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.ActivitiesRequest
import io.getstream.feed.client.internal.api.models.UpdateActivitiesRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByForeignIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityByIdRequest
import io.getstream.feed.client.internal.api.models.UpdateActivityRequest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert

internal class RequestAdapterFactoryTest {

    private val moshi = FeedMoshiConverterFactory.moshi

    /** [toJsonArgs] */
    @ParameterizedTest
    @MethodSource("toJsonArgs")
    fun testToJson(clazz: Class<Any>, request: Any, expectedJsonString: String) {
        JSONAssert.assertEquals(expectedJsonString, moshi.adapter(clazz).toJson(request), true)
    }

    companion object {
        private val set: Map<String, Any> = mapOf(
            "String" to "Some String",
            "Int" to 846512,
            "Array" to listOf(1, 2, 3)
        )
        val setJsonString: String = """
            {
            "String" : "Some String",
            "Int" : 846512,
            "Array" : [1 , 2, 3]
            }
        """.trimIndent()

        @JvmStatic
        fun toJsonArgs() = listOf(
            randomUpdateActivitiesRequest().let {
                Arguments.of(UpdateActivitiesRequest::class.java, it, it.toJsonString())
            },
            randomUpdateActivityByIdRequest(set = set).let {
                Arguments.of(UpdateActivityRequest::class.java, it, it.toJsonString(setJsonString))
            },
            randomUpdateActivityByIdRequest(set = set).let {
                Arguments.of(UpdateActivityByIdRequest::class.java, it, it.toJsonString(setJsonString))
            },
            randomUpdateActivityByIdRequest().let {
                Arguments.of(UpdateActivityRequest::class.java, it, it.toJsonString())
            },
            randomUpdateActivityByIdRequest().let {
                Arguments.of(UpdateActivityByIdRequest::class.java, it, it.toJsonString())
            },
            randomUpdateActivityByForeignIdRequest(set = set).let {
                Arguments.of(UpdateActivityRequest::class.java, it, it.toJsonString(setJsonString))
            },
            randomUpdateActivityByForeignIdRequest(set = set).let {
                Arguments.of(UpdateActivityByForeignIdRequest::class.java, it, it.toJsonString(setJsonString))
            },
            randomUpdateActivityByForeignIdRequest().let {
                Arguments.of(UpdateActivityRequest::class.java, it, it.toJsonString())
            },
            randomUpdateActivityByForeignIdRequest().let {
                Arguments.of(UpdateActivityByForeignIdRequest::class.java, it, it.toJsonString())
            },
            randomActivitiesRequest().let {
                Arguments.of(ActivitiesRequest::class.java, it, it.toJsonString())
            },
        )
    }
}
