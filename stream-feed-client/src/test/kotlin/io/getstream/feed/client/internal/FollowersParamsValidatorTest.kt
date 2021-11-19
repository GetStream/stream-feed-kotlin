package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.FollowersParams
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.NegativeParamError
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class FollowersParamsValidatorTest {

    /**
     * Use [getFollowersParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getFollowersParamsArgs")
    fun `Should return either the same FollowersParams or an StreamError`(
        followersParams: FollowersParams,
        expectedResult: Either<StreamError, FollowersParams>
    ) {
        followersParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getFollowersParamsArgs() = validFollowersParamsArgs() + invalidFollowersParamsArgs()

        @JvmStatic
        private fun validFollowersParamsArgs() = listOf(
            FollowersParams().let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply { limit = 0 }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply { limit = positiveRandomInt() }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply { limit = null }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply { offset = 0 }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply { offset = positiveRandomInt() }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply { offset = null }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = 0
                    offset = 0
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = positiveRandomInt()
                    offset = 0
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = null
                    offset = 0
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = 0
                    offset = positiveRandomInt()
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = positiveRandomInt()
                    offset = positiveRandomInt()
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = null
                    offset = positiveRandomInt()
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = 0
                    offset = null
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = positiveRandomInt()
                    offset = null
                }
                .let { Arguments.of(it, it.right()) },
            FollowersParams()
                .apply {
                    limit = null
                    offset = null
                }
                .let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidFollowersParamsArgs() = listOf(
            Arguments.of(
                FollowersParams().apply { limit = negativeRandomInt() },
                NegativeParamError("limit can't be negative").left()
            ),
            Arguments.of(
                FollowersParams().apply { offset = negativeRandomInt() },
                NegativeParamError("offset can't be negative").left()
            ),
        )
    }
}
