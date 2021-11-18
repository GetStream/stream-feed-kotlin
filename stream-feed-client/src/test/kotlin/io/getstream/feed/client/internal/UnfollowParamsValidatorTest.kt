package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.Mother.randomBoolean
import io.getstream.feed.client.Mother.randomFeedID
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.UnfollowParams
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class UnfollowParamsValidatorTest {

    /**
     * Use [getUnfollowParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getUnfollowParamsArgs")
    fun `Should return either the same UnfollowParams or an StreamError`(
        unfollowParams: UnfollowParams,
        expectedResult: Either<StreamError, UnfollowParams>
    ) {
        unfollowParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getUnfollowParamsArgs() = validUnfollowParamsArgs() + invalidUnfollowParamsArgs()

        @JvmStatic
        private fun validUnfollowParamsArgs() = listOf(
            UnfollowParams()
                .apply {
                    targetFeedID = randomFeedID()
                }
                .let { Arguments.of(it, it.right()) },
            UnfollowParams()
                .apply {
                    targetFeedID = randomFeedID()
                    keepHistory = randomBoolean()
                }
                .let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidUnfollowParamsArgs() = listOf(
            Arguments.of(UnfollowParams(), EmptyParamError("targetFeedID property need to be initialized").left()),
            Arguments.of(
                UnfollowParams().apply { keepHistory = randomBoolean() },
                EmptyParamError("targetFeedID property need to be initialized").left()
            ),
        )
    }
}
