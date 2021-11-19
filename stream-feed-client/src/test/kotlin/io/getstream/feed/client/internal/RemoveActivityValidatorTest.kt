package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.RemoveActivityByForeignId
import io.getstream.feed.client.RemoveActivityById
import io.getstream.feed.client.RemoveActivityParams
import io.getstream.feed.client.StreamError
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class RemoveActivityValidatorTest {

    /**
     * Use [getRemoveActivityParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("getRemoveActivityParamsArgs")
    fun `Should return either the same RemoveActivityParams or an StreamError`(
        removeActivityParams: RemoveActivityParams,
        expectedResult: Either<StreamError, RemoveActivityParams>
    ) {
        removeActivityParams.validate() `should be equal to` expectedResult
    }

    companion object {

        @JvmStatic
        fun getRemoveActivityParamsArgs() = validRemoveActivityParamsArgs() + invalidRemoveActivityParamsArgs()

        @JvmStatic
        private fun validRemoveActivityParamsArgs() = listOf(
            RemoveActivityById(randomString()).let { Arguments.of(it, it.right()) },
            RemoveActivityByForeignId(randomString()).let { Arguments.of(it, it.right()) },
        )

        @JvmStatic
        private fun invalidRemoveActivityParamsArgs() = listOf(
            RemoveActivityById("").let { Arguments.of(it, EmptyParamError("activityId can't be empty").left()) },
            RemoveActivityByForeignId("").let { Arguments.of(it, EmptyParamError("foreignId can't be empty").left()) },
            RemoveActivityById("  ").let { Arguments.of(it, EmptyParamError("activityId can't be empty").left()) },
            RemoveActivityByForeignId("  ").let { Arguments.of(it, EmptyParamError("foreignId can't be empty").left()) },
        )
    }
}
