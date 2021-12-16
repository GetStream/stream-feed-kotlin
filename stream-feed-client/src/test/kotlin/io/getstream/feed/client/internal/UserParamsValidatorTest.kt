package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.Mother.negativeRandomInt
import io.getstream.feed.client.Mother.positiveRandomInt
import io.getstream.feed.client.Mother.randomBoolean
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.UserParams
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class UserParamsValidatorTest {

    /**
     * Use [userParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("userParamsArgs")
    fun `Should return either the same UserParams or an StreamError`(
        userParams: UserParams,
        expectedResult: Either<StreamError, UserParams>
    ) {
        userParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun userParamsArgs() = validUserParamsArgs() + invalidUserParamsArgs()

        @JvmStatic
        private fun validUserParamsArgs() = listOf(
            UserParams().apply { userId = randomString() }.let { Arguments.of(it, it.right()) },
            UserParams().apply {
                userId = randomString()
                data = mapOf(
                    randomString() to randomString(),
                    randomString() to positiveRandomInt(),
                    randomString() to negativeRandomInt(),
                    randomString() to randomBoolean(),
                )
            }.let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidUserParamsArgs() = listOf(
            Arguments.of(
                UserParams(),
                EmptyParamError("userId property need to be initialized").left()
            ),
            Arguments.of(
                UserParams().apply {
                    data = mapOf(
                        randomString() to randomString(),
                        randomString() to positiveRandomInt(),
                        randomString() to negativeRandomInt(),
                        randomString() to randomBoolean(),
                    )
                },
                EmptyParamError("userId property need to be initialized").left()
            ),
        )
    }
}
