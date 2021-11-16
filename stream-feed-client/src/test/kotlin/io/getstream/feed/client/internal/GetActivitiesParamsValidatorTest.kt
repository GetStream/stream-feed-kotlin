package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.GetActivitiesParams
import io.getstream.feed.client.IncompatibleParamsError
import io.getstream.feed.client.Mother.createGetActivitiesParams
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class GetActivitiesParamsValidatorTest {

    /**
     * Use [getActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getActivitiesParamsArgs")
    fun `Should return the same GetActivitiesParams calling to validate method`(
        getActivitiesParams: GetActivitiesParams,
        expectedResult: Either<StreamError, GetActivitiesParams>
    ) {
        getActivitiesParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getActivitiesParamsArgs() = validGetActivitiesParamsArgs() + invalidGetActivitiesParamsArgs()

        @JvmStatic
        fun validGetActivitiesParamsArgs() = listOf(
            createGetActivitiesParams().let { Arguments.of(it, it.right()) },
            createGetActivitiesParams { limit = 0 }.let { Arguments.of(it, it.right()) },
            createGetActivitiesParams { limit = positiveRandomInt() }.let { Arguments.of(it, it.right()) },
            createGetActivitiesParams { offset = 0 }.let { Arguments.of(it, it.right()) },
            createGetActivitiesParams { offset = null }.let { Arguments.of(it, it.right()) },
            createGetActivitiesParams { withRecentReactions(null) }.let { Arguments.of(it, it.right()) },
            createGetActivitiesParams { withRecentReactions(positiveRandomInt()) }.let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        fun invalidGetActivitiesParamsArgs() = listOf(
            Arguments.of(
                createGetActivitiesParams { limit = negativeRandomInt() },
                NegativeParamError("limit can't be negative").left()
            ),
            Arguments.of(
                createGetActivitiesParams { offset = negativeRandomInt() },
                NegativeParamError("offset can't be negative").left()
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThan = randomString()
                    idGreaterThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan and idGreaterThanOrEqual is not supported").left()
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThanOrEqual = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThanOrEqual = randomString()
                    idSmallerThan = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThan = randomString()
                    idSmallerThan = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idGreaterThan = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createGetActivitiesParams {
                    idSmallerThan = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idSmallerThan and idSmallerThanOrEqual is not supported").left()
            ),
            Arguments.of(
                createGetActivitiesParams { withRecentReactions(negativeRandomInt()) },
                NegativeParamError("recentReactionsLimit can't be negative").left()
            ),
        )
    }
}
