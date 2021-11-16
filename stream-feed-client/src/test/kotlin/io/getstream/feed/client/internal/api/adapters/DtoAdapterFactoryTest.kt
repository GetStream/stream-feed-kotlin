package io.getstream.feed.client.internal.api.adapters

import io.getstream.feed.client.JSONMother.toJsonString
import io.getstream.feed.client.Mother.randomActorDto
import io.getstream.feed.client.Mother.randomDownstreamActivityDto
import io.getstream.feed.client.Mother.randomDownstreamEnrichActivityDto
import io.getstream.feed.client.Mother.randomUpstreamActivityDto
import io.getstream.feed.client.Mother.randomUpstreamEnrichActivitySDto
import io.getstream.feed.client.internal.api.models.ActivitySealedDto
import io.getstream.feed.client.internal.api.models.ActorDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.DownstreamActivitySealedDto
import io.getstream.feed.client.internal.api.models.DownstreamEnrichActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivitySealedDto
import io.getstream.feed.client.internal.api.models.UpstreamEnrichActivityDto
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.skyscreamer.jsonassert.JSONAssert

internal class DtoAdapterFactoryTest {

    private val moshi = FeedMoshiConverterFactory.moshi

    /** [toJsonArgs] */
    @ParameterizedTest
    @MethodSource("toJsonArgs")
    fun testToJson(clazz: Class<Any>, dto: Any, expectedJsonString: String) {
        JSONAssert.assertEquals(expectedJsonString, moshi.adapter(clazz).toJson(dto), true)
    }

    /** [fromJsonArgs] */
    @ParameterizedTest
    @MethodSource("fromJsonArgs")
    fun testFromJson(clazz: Class<Any>, jsonString: String, expectedDto: Any) {
        moshi.adapter(clazz).fromJson(jsonString) `should be equal to` expectedDto
    }

    companion object {
        private val extraData: MutableMap<String, Any> = mutableMapOf(
            "String" to "Some String",
            "Number" to 846512.0,
            "Array" to listOf(1.0, 2.0, 3.0)
        )
        private val extraDataJsonString: String = """
            "String" : "Some String",
            "Number" : 846512,
            "Array" : [1 , 2, 3]
        """.trimIndent()

        private val extraDataJsonStringWithNullValues = extraDataJsonString + """,
            "someNullValueKey": null
        """.trimMargin()

        @JvmStatic
        fun toJsonArgs() = listOf(
            randomActorDto(extraData = extraData).let {
                Arguments.of(ActorDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamActivityDto(extraData = extraData).let {
                Arguments.of(UpstreamActivityDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamActivityDto(extraData = extraData).let {
                Arguments.of(UpstreamActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamEnrichActivitySDto(extraData = extraData).let {
                Arguments.of(UpstreamEnrichActivityDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamEnrichActivitySDto(extraData = extraData).let {
                Arguments.of(UpstreamActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamEnrichActivitySDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivityDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamEnrichActivityDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
        )

        @JvmStatic
        fun fromJsonArgs() = listOf(
            randomActorDto(extraData = extraData).let {
                Arguments.of(ActorDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomActorDto(extraData = extraData).let {
                Arguments.of(ActorDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivityDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivityDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivitySealedDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivitySealedDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamEnrichActivityDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamEnrichActivityDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivitySealedDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivitySealedDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamEnrichActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
        )
    }
}
