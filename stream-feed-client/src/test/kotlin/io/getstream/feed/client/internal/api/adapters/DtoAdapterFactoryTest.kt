package io.getstream.feed.client.internal.api.adapters

import io.getstream.feed.client.JSONMother.toIdStringJsonString
import io.getstream.feed.client.JSONMother.toJsonString
import io.getstream.feed.client.Mother.randomDataDto
import io.getstream.feed.client.Mother.randomDownstreamActivityDto
import io.getstream.feed.client.Mother.randomUpstreamActivityDto
import io.getstream.feed.client.internal.api.adapters.DtoAdapterFactoryTest.Companion.fromJsonArgs
import io.getstream.feed.client.internal.api.models.ActivitySealedDto
import io.getstream.feed.client.internal.api.models.DataDto
import io.getstream.feed.client.internal.api.models.DownstreamActivityDto
import io.getstream.feed.client.internal.api.models.UpstreamActivityDto
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
            randomUpstreamActivityDto(extraData = extraData).let {
                Arguments.of(UpstreamActivityDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomUpstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivityDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it, it.toJsonString(extraDataJsonString))
            },
            randomDataDto(data = extraData).let {
                Arguments.of(DataDto::class.java, it, it.toIdStringJsonString())
            },
        )

        @JvmStatic
        fun fromJsonArgs() = listOf(
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivityDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(DownstreamActivityDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDownstreamActivityDto(extraData = extraData).let {
                Arguments.of(ActivitySealedDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
            randomDataDto().let { Arguments.of(DataDto::class.java, it.toIdStringJsonString(), it) },
            randomDataDto().let { Arguments.of(DataDto::class.java, it.toJsonString(), it) },
            randomDataDto().let { Arguments.of(DataDto::class.java, it.toJsonString("    "), it) },
            randomDataDto(data = extraData).let {
                Arguments.of(DataDto::class.java, it.toJsonString(extraDataJsonString), it)
            },
            randomDataDto(data = extraData).let {
                Arguments.of(DataDto::class.java, it.toJsonString(extraDataJsonStringWithNullValues), it)
            },
        )
    }
}
