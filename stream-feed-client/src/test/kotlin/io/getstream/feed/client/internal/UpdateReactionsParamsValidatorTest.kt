package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomBoolean
import io.getstream.feed.client.Mother.randomFeedID
import io.getstream.feed.client.Mother.randomListOf
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.UpdateReactionParams
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class UpdateReactionsParamsValidatorTest {

    /**
     * Use [updateReactionParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("updateReactionParamsArgs")
    fun `Should return either the same UpdateReactionParams or an StreamError`(
        updateReactionParams: UpdateReactionParams,
        expectedResult: Either<StreamError, UpdateReactionParams>
    ) {
        updateReactionParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun updateReactionParamsArgs() = validUpdateReactionParamsArgs() + invalidUpdateReactionParamsArgs()

        @JvmStatic
        private fun validUpdateReactionParamsArgs() = listOf(
            UpdateReactionParams().apply { reactionId = randomString() }.let { Arguments.of(it, it.right()) },
            UpdateReactionParams().apply {
                reactionId = randomString()
                data = mapOf(
                    randomString() to randomString(),
                    randomString() to positiveRandomInt(),
                    randomString() to negativeRandomInt(),
                    randomString() to randomBoolean(),
                )
            }.let { Arguments.of(it, it.right()) },
            UpdateReactionParams().apply {
                reactionId = randomString()
                targetFeeds = randomListOf { randomFeedID() }
            }.let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidUpdateReactionParamsArgs() = listOf(
            Arguments.of(
                UpdateReactionParams(),
                EmptyParamError("reactionId property need to be initialized").left()
            ),
            Arguments.of(
                UpdateReactionParams().apply {
                    data = mapOf(
                        randomString() to randomString(),
                        randomString() to positiveRandomInt(),
                        randomString() to negativeRandomInt(),
                        randomString() to randomBoolean(),
                    )
                },
                EmptyParamError("reactionId property need to be initialized").left()
            ),
            Arguments.of(
                UpdateReactionParams().apply { targetFeeds = randomListOf { randomFeedID() } },
                EmptyParamError("reactionId property need to be initialized").left()
            ),
        )
    }
}
