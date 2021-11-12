package io.getstream.feed.client.internal

import io.getstream.feed.client.GetActivitiesParams
import io.getstream.feed.client.Mother.createGetActivitiesParams
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomString
import org.amshove.kluent.invoking
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.`with message`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class GetActivitiesParamsValidatorTest {

    /**
     * Use [validGetActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("validGetActivitiesParamsArgs")
    fun `Should return the same GetActivitiesParams calling to validate method`(getActivitiesParams: GetActivitiesParams) {
        getActivitiesParams.validate() `should be` getActivitiesParams
    }

    /**
     * Use [invalidGetActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("invalidGetActivitiesParamsArgs")
    fun `Should throw exception calling to validate method`(
        getActivitiesParams: GetActivitiesParams,
        expectedErrorMessage: String,
    ) {
        invoking { getActivitiesParams.validate() } `should throw` IllegalArgumentException::class `with message` expectedErrorMessage
    }

    companion object {

        @JvmStatic
        fun validGetActivitiesParamsArgs() = listOf(
            Arguments.of(createGetActivitiesParams()),
            Arguments.of(createGetActivitiesParams { limit = positiveRandomInt() }),
            Arguments.of(createGetActivitiesParams { offset = 0 }),
            Arguments.of(createGetActivitiesParams { offset = null }),
            Arguments.of(createGetActivitiesParams { withRecentReactions(null) }),
            Arguments.of(createGetActivitiesParams { withRecentReactions(positiveRandomInt()) }),
        )

        @JvmStatic
        fun invalidGetActivitiesParamsArgs() = listOf(
            Arguments.of(createGetActivitiesParams { limit = negativeRandomInt() }, "limit can't be negative"),
            Arguments.of(createGetActivitiesParams { offset = negativeRandomInt() }, "offset can't be negative"),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThan = randomString()
                    idGreaterThanOrEqual = randomString()
                },
                "Passing both idGreaterThan and idGreaterThanOrEqual is not supported"
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThanOrEqual = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                "Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported"
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThanOrEqual = randomString()
                    idSmallerThan = randomString()
                },
                "Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported"
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThan = randomString()
                    idSmallerThan = randomString()
                },
                "Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported"
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThan = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                "Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported"
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idSmallerThan = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                "Passing both idSmallerThan and idSmallerThanOrEqual is not supported"
            ),
            Arguments.of(
                createGetActivitiesParams { withRecentReactions(negativeRandomInt()) },
                "recentReactionsLimit can't be negative"
            ),
        )
    }
}
