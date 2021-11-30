package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.FindActivitiesParams
import io.getstream.feed.client.IncompatibleParamsError
import io.getstream.feed.client.InvalidParamError
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomDifferentThan
import io.getstream.feed.client.Mother.randomListOf
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class FindActivitiesParamsValidatorTest {

    /**
     * Use [findActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("findActivitiesParamsArgs")
    fun `Should return either the same FindActivitiesParams or an StreamError`(
        findActivitiesParams: FindActivitiesParams,
        expectedResult: Either<StreamError, FindActivitiesParams>
    ) {
        findActivitiesParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun findActivitiesParamsArgs() = validFindActivitiesParamsArgs() + invalidFindActivitiesParamsArgs()

        @JvmStatic
        private fun validFindActivitiesParamsArgs() = listOf(
            FindActivitiesParams().let { Arguments.of(it, it.right()) },
            FindActivitiesParams()
                .apply { activitiesIds = randomListOf(positiveRandomInt(100), ::randomString) }
                .let { Arguments.of(it, it.right()) },
            FindActivitiesParams()
                .apply {
                    val randomNumber = positiveRandomInt(100)
                    activitiesIds = randomListOf(positiveRandomInt(100), ::randomString)
                    foreignIds = randomListOf(randomNumber, ::randomString)
                    timestamps = randomListOf(randomNumber, ::randomString)
                }
                .let { Arguments.of(it, it.right()) },
            FindActivitiesParams()
                .apply {
                    val randomNumber = positiveRandomInt(100)
                    foreignIds = randomListOf(randomNumber, ::randomString)
                    timestamps = randomListOf(randomNumber, ::randomString)
                }
                .let { Arguments.of(it, it.right()) },
            FindActivitiesParams()
                .apply { withRecentReactions(null) }
                .let { Arguments.of(it, it.right()) },
            FindActivitiesParams()
                .apply { withRecentReactions(positiveRandomInt()) }
                .let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidFindActivitiesParamsArgs() = listOf(
            Arguments.of(
                FindActivitiesParams()
                    .apply { activitiesIds = randomListOf(positiveRandomInt(1000) + 100, ::randomString) },
                InvalidParamError("activitiesIds can't be bigger than 100").left()
            ),
            Arguments.of(
                FindActivitiesParams()
                    .apply {
                        val randomNumber = positiveRandomInt(1000) + 100
                        activitiesIds = randomListOf(positiveRandomInt(100), ::randomString)
                        foreignIds = randomListOf(randomNumber, ::randomString)
                        timestamps = randomListOf(randomNumber, ::randomString)
                    },
                InvalidParamError("foreignIds can't be bigger than 100").left()
            ),
            Arguments.of(
                FindActivitiesParams()
                    .apply {
                        activitiesIds = randomListOf(positiveRandomInt(100), ::randomString)
                        foreignIds = randomListOf(positiveRandomInt(100), ::randomString)
                        timestamps = randomListOf(positiveRandomInt(1000) + 100, ::randomString)
                    },
                InvalidParamError("timestamps can't be bigger than 100").left()
            ),
            Arguments.of(
                FindActivitiesParams()
                    .apply {
                        val randomNumberOfForeignIds = positiveRandomInt(100)
                        val randomNumberOfTimestamps =
                            randomDifferentThan(randomNumberOfForeignIds) { positiveRandomInt(100) }
                        activitiesIds = randomListOf(positiveRandomInt(100), ::randomString)
                        foreignIds = randomListOf(randomNumberOfForeignIds, ::randomString)
                        timestamps = randomListOf(randomNumberOfTimestamps, ::randomString)
                    },
                IncompatibleParamsError("foreignIds length must match timestamps length").left()
            ),
            Arguments.of(
                FindActivitiesParams().apply { withRecentReactions(negativeRandomInt()) },
                NegativeParamError("recentReactionsLimit can't be negative").left()
            ),
        )
    }
}
