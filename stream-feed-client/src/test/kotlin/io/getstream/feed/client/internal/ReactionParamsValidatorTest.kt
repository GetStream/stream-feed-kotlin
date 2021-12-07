package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.Mother.randomReaction
import io.getstream.feed.client.Reaction
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class ReactionParamsValidatorTest {

    /**
     * Use [reactionParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("reactionParamsArgs")
    fun `Should return either the same Reaction or an StreamError`(
        reaction: Reaction,
        expectedResult: Either<StreamError, Reaction>
    ) {
        reaction.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun reactionParamsArgs() = validReactionParamsArgs() + invalidReactionParamsArgs()

        @JvmStatic
        private fun validReactionParamsArgs() = listOf(
            randomReaction().let { Arguments.of(it, it.right()) }
        )

        @JvmStatic
        private fun invalidReactionParamsArgs() = listOf(
            Arguments.of(
                randomReaction(kind = ""),
                EmptyParamError("kind can't be empty").left()
            ),
            Arguments.of(
                randomReaction(kind = "   "),
                EmptyParamError("kind can't be empty").left()
            ),
            Arguments.of(
                randomReaction(activityId = ""),
                EmptyParamError("activityId can't be empty").left()
            ),
            Arguments.of(
                randomReaction(activityId = "   "),
                EmptyParamError("activityId can't be empty").left()
            ),
        )
    }
}
