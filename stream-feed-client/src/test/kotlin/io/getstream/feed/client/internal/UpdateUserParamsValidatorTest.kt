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
import io.getstream.feed.client.UpdateUserParams
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class UpdateUserParamsValidatorTest {

    /**
     * Use [updateUserParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("updateUserParamsArgs")
    fun `Should return either the same UpdateUserParams or an StreamError`(
        updateUserParams: UpdateUserParams,
        expectedResult: Either<StreamError, UpdateUserParams>
    ) {
        updateUserParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun updateUserParamsArgs() = validUpdateUserParamsArgs() + invalidUpdateUserParamsArgs()

        @JvmStatic
        private fun validUpdateUserParamsArgs() = listOf(
            UpdateUserParams().apply { userId = randomString() }.let { Arguments.of(it, it.right()) },
            UpdateUserParams().apply {
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
        private fun invalidUpdateUserParamsArgs() = listOf(
            Arguments.of(
                UpdateUserParams(),
                EmptyParamError("userId property need to be initialized").left()
            ),
            Arguments.of(
                UpdateUserParams().apply {
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
