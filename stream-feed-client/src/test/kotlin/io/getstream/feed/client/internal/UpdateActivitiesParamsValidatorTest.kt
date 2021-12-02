package io.getstream.feed.client.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.getstream.feed.client.EmptyParamError
import io.getstream.feed.client.Mother.randomString
import io.getstream.feed.client.ParamError
import io.getstream.feed.client.StreamError
import io.getstream.feed.client.UpdateActivityByForeignIdParams
import io.getstream.feed.client.UpdateActivityByIdParams
import io.getstream.feed.client.UpdateActivityParams
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class UpdateActivitiesParamsValidatorTest {

    /**
     * Use [updateActivityParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("updateActivityParamsArgs")
    fun `Should return either the same UpdateActivityParams or an StreamError`(
        updateActivitiesParams: UpdateActivityParams,
        expectedResult: Either<StreamError, UpdateActivityParams>
    ) {
        updateActivitiesParams.validate() `should be equal to` expectedResult
    }

    /**
     * Use [updateActivitiesParamsArgs] as arguments
     */
    @ParameterizedTest
    @MethodSource("updateActivitiesParamsArgs")
    fun `Should return either the same List of UpdateActivityParams or an StreamError`(
        updateActivitiesParamsList: List<UpdateActivityParams>,
        expectedResult: Either<StreamError, List<UpdateActivityParams>>
    ) {
        updateActivitiesParamsList.validate() `should be equal to` expectedResult
    }

    companion object {

        private fun validUpdateActivitiesParams(): List<UpdateActivityParams> = listOf(
            UpdateActivityByForeignIdParams().apply {
                foreignId = randomString()
                time = randomString()
            },
            UpdateActivityByIdParams().apply { activityId = randomString() }
        )

        private fun invalidUpdateActivitiesParams(): List<Pair<UpdateActivityParams, ParamError>> = listOf(
            UpdateActivityByForeignIdParams().apply {
                time = randomString()
            } to EmptyParamError("foreignId and time properties need to be initialized"),
            UpdateActivityByForeignIdParams().apply {
                foreignId = randomString()
            } to EmptyParamError("foreignId and time properties need to be initialized"),
            UpdateActivityByIdParams() to EmptyParamError("activityId property need to be initialized"),
        )

        @JvmStatic
        fun updateActivityParamsArgs() = validUpdateActivitiesParamsArgs() + invalidUpdateActivitiesParamsArgs()

        @JvmStatic
        fun updateActivitiesParamsArgs() = invalidUpdateActivitiesParams().map { invalidUpdateActivitiesParams ->
            (validUpdateActivitiesParams() + invalidUpdateActivitiesParams.first).shuffled().let {
                Arguments.of(it, invalidUpdateActivitiesParams.second.left())
            }
        } +
            validUpdateActivitiesParams().let { Arguments.of(it, it.right()) } +
            Arguments.of(emptyList<UpdateActivityParams>(), EmptyParamError("The list of updateActivities can't be empty").left())

        @JvmStatic
        fun validUpdateActivitiesParamsArgs() = validUpdateActivitiesParams().map {
            Arguments.of(it, it.right())
        }

        @JvmStatic
        fun invalidUpdateActivitiesParamsArgs() = invalidUpdateActivitiesParams().map {
            Arguments.of(it.first, it.second.left())
        }
    }
}
