package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.FollowParams
import io.getstream.feed.client.InvalidParamError
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomFeedID
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.internal.GetActivitiesParamsValidatorTest.Companion.getActivitiesParamsArgs
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class FollowParamsValidatorTest {

    /**
     * Use [getActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getActivitiesParamsArgs")
    fun `Should return either the same FollowParam or an StreamError`(
        followParams: FollowParams,
        expectedResult: Either<StreamError, FollowParams>
    ) {
        followParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getActivitiesParamsArgs() = validGetActivitiesParamsArgs() + invalidGetActivitiesParamsArgs()

        @JvmStatic
        private fun validGetActivitiesParamsArgs() = listOf(
            FollowParams()
                .apply {
                    targetFeedID = randomFeedID()
                }
                .let { Arguments.of(it, it.right()) },
            FollowParams()
                .apply {
                    targetFeedID = randomFeedID()
                    activityCopyLimit = positiveRandomInt(1000)
                }
                .let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidGetActivitiesParamsArgs() = listOf(
            Arguments.of(FollowParams(), EmptyParamError("targetFeedID property need to be initialized").left()),
            Arguments.of(
                FollowParams().apply { activityCopyLimit = positiveRandomInt(1000) },
                EmptyParamError("targetFeedID property need to be initialized").left()
            ),
            Arguments.of(
                FollowParams()
                    .apply {
                        targetFeedID = randomFeedID()
                        activityCopyLimit = 1000 + positiveRandomInt(1000)
                    },
                InvalidParamError("activityCopyLimit can't be bigger than 1000").left()
            ),
            Arguments.of(
                FollowParams()
                    .apply {
                        targetFeedID = randomFeedID()
                        activityCopyLimit = negativeRandomInt()
                    },
                NegativeParamError("activityCopyLimit can't be negative").left()
            ),
        )
    }
}
