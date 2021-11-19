package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.FollowedParams
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class FollowedParamsValidatorTest {

    /**
     * Use [getFollowedParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getFollowedParamsArgs")
    fun `Should return either the same FollowedParams or an StreamError`(
        followedParams: FollowedParams,
        expectedResult: Either<StreamError, FollowedParams>
    ) {
        followedParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getFollowedParamsArgs() = validFollowedParamsArgs() + invalidFollowedParamsArgs()

        @JvmStatic
        private fun validFollowedParamsArgs() = listOf(
            FollowedParams().let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply { limit = 0 }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply { limit = positiveRandomInt() }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply { limit = null }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply { offset = 0 }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply { offset = positiveRandomInt() }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply { offset = null }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = 0
                    offset = 0
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = positiveRandomInt()
                    offset = 0
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = null
                    offset = 0
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = 0
                    offset = positiveRandomInt()
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = positiveRandomInt()
                    offset = positiveRandomInt()
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = null
                    offset = positiveRandomInt()
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = 0
                    offset = null
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = positiveRandomInt()
                    offset = null
                }
                .let { Arguments.of(it, it.right()) },
            FollowedParams()
                .apply {
                    limit = null
                    offset = null
                }
                .let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidFollowedParamsArgs() = listOf(
            Arguments.of(
                FollowedParams().apply { limit = negativeRandomInt() },
                NegativeParamError("limit can't be negative").left()
            ),
            Arguments.of(
                FollowedParams().apply { offset = negativeRandomInt() },
                NegativeParamError("offset can't be negative").left()
            ),
        )
    }
}
