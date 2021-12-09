package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.FilterReactionsParams
import io.getstream.feed.client.IncompatibleParamsError
import io.getstream.feed.client.Mother.createFilterReactionsParams
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class FilterReactionsParamsValidatorTest {

    /**
     * Use [filterReactionsParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("filterReactionsParamsArgs")
    fun `Should return either the same FilterReactionsParams or an StreamError`(
        filterReactionsParams: FilterReactionsParams,
        expectedResult: Either<StreamError, FilterReactionsParams>
    ) {
        filterReactionsParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun filterReactionsParamsArgs() = validFilterReactionsParamsArgs() + invalidFilterReactionsParamsArgs()

        @JvmStatic
        private fun validFilterReactionsParamsArgs() = listOf(
            createFilterReactionsParams().let { Arguments.of(it, it.right()) },
            createFilterReactionsParams { limit = 0 }.let { Arguments.of(it, it.right()) },
            createFilterReactionsParams { limit = positiveRandomInt() }.let { Arguments.of(it, it.right()) },
            createFilterReactionsParams { idGreaterThan = randomString() }.let { Arguments.of(it, it.right()) },
            createFilterReactionsParams { idGreaterThanOrEqual = randomString() }.let { Arguments.of(it, it.right()) },
            createFilterReactionsParams { idSmallerThan = randomString() }.let { Arguments.of(it, it.right()) },
            createFilterReactionsParams { idSmallerThanOrEqual = randomString() }.let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidFilterReactionsParamsArgs() = listOf(
            Arguments.of(
                FilterReactionsParams(),
                EmptyParamError("lookup property need to be initialized").left()
            ),
            Arguments.of(
                createFilterReactionsParams { limit = negativeRandomInt() },
                NegativeParamError("limit can't be negative").left()
            ),
            Arguments.of(
                createFilterReactionsParams {
                    idGreaterThan = randomString()
                    idGreaterThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan and idGreaterThanOrEqual is not supported").left()
            ),
            Arguments.of(
                createFilterReactionsParams {
                    idGreaterThanOrEqual = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createFilterReactionsParams {
                    idGreaterThanOrEqual = randomString()
                    idSmallerThan = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createFilterReactionsParams {
                    idGreaterThan = randomString()
                    idSmallerThan = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createFilterReactionsParams {
                    idGreaterThan = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idGreaterThan[OrEqual] and idSmallerThan[OrEqual] is not supported").left()
            ),
            Arguments.of(
                createFilterReactionsParams {
                    idSmallerThan = randomString()
                    idSmallerThanOrEqual = randomString()
                },
                IncompatibleParamsError("Passing both idSmallerThan and idSmallerThanOrEqual is not supported").left()
            ),
        )
    }
}
