package io.getstream.feed.client.internal.api.adapters

import io.getstream.feed.client.internal.api.models.CommaSeparatedQueryParams
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class CommaSeparatedQueryParamsConverterTest {

    /** [queryParamsArgs] */
    @ParameterizedTest
    @MethodSource("queryParamsArgs")
    fun `Should generate a proper query param`(queryParams: CommaSeparatedQueryParams<*>, expectedStringQueryParams: String?) {
        CommaSeparatedQueryParamsConverter.convert(queryParams) `should be equal to` expectedStringQueryParams
    }

    companion object {

        @JvmStatic
        fun queryParamsArgs() = listOf(
            Arguments.of(CommaSeparatedQueryParams(emptyList<String>()), null),
            Arguments.of(CommaSeparatedQueryParams(listOf(1)), "1"),
            Arguments.of(CommaSeparatedQueryParams(listOf(1, 2)), "1,2"),
            Arguments.of(CommaSeparatedQueryParams(listOf(1, 2, "Hello")), "1,2,Hello"),
            Arguments.of(CommaSeparatedQueryParams(listOf(1, 2, 2.2)), "1,2,2.2"),
            Arguments.of(CommaSeparatedQueryParams(listOf(1, 2, 2.2)), "1,2,2.2"),
            Arguments.of(CommaSeparatedQueryParams(listOf("id:1", "id;2")), "id:1,id;2"),
        )
    }
}
