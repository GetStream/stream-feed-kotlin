package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.FindUserParams
import io.getstream.feed.client.Mother.randomBoolean
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class FIndUserParamsValidatorTest {

    /**
     * Use [findUserParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("findUserParamsArgs")
    fun `Should return either the same FindUserParams or an StreamError`(
        findUserParams: FindUserParams,
        expectedResult: Either<StreamError, FindUserParams>
    ) {
        findUserParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun findUserParamsArgs() = validFindUserParamsArgs() + invalidFindUserParamsArgs()

        @JvmStatic
        private fun validFindUserParamsArgs() = listOf(
            FindUserParams().apply { userId = randomString() }.let { Arguments.of(it, it.right()) },
            FindUserParams().apply {
                userId = randomString()
                withFollowCount = randomBoolean()
            }.let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidFindUserParamsArgs() = listOf(
            Arguments.of(
                FindUserParams(),
                EmptyParamError("userId property need to be initialized").left()
            ),
            Arguments.of(
                FindUserParams().apply { withFollowCount = randomBoolean() },
                EmptyParamError("userId property need to be initialized").left()
            ),
        )
    }
}
