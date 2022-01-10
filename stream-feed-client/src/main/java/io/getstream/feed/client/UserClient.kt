package io.getstream.feed.client

import arrow.core.Either
import arrow.core.computations.either
import io.getstream.feed.client.internal.api.UserApi
import io.getstream.feed.client.internal.api.models.UpdateUserRequest
import io.getstream.feed.client.internal.api.models.UserDto
import io.getstream.feed.client.internal.api.models.UserRequest
import io.getstream.feed.client.internal.obtainEntity
import io.getstream.feed.client.internal.toDomain
import io.getstream.feed.client.internal.validate

/**
 * Client that allows you to work with users.
 *
 * @property userApi The users endpoint.
 */
class UserClient internal constructor(private val userApi: UserApi) {

    /**
     * Get a User that matches the given params.
     *
     * @param params A function over [FindUserParams] that defines params to be used.
     * @return An [Either] with the [User] or an [StreamError].
     */
    suspend fun getUser(params: FindUserParams.() -> Unit = {}): Either<StreamError, User> = either {
        val findUserParams = FindUserParams()
            .apply(params)
            .validate()
            .bind()
        userApi.getUser(findUserParams.userId, findUserParams.withFollowCount)
            .obtainEntity()
            .map(UserDto::toDomain)
            .bind()
    }

    /**
     * Get a User that matches the given params or create it if doesn't exist.
     *
     * @param params A function over [UserParams] that define params to be used.
     * @return An [Either] with the [User] or an [StreamError].
     */
    suspend fun getOrCreateUser(params: UserParams.() -> Unit = {}): Either<StreamError, User> = addUser(UserParams().apply(params))

    /**
     * Create a User with given params.
     *
     * @param params A function over [UserParams] that define params to be used.
     * @return An [Either] with the [User] or an [StreamError].
     */
    suspend fun createUser(params: UserParams.() -> Unit = {}): Either<StreamError, User> = addUser(UserParams().apply(params))

    /**
     * Create a User.
     *
     * @param user The user to be created.
     * @return An [Either] with the [User] or an [StreamError].
     */
    suspend fun createUser(user: User): Either<StreamError, User> =
        createUser {
            userId = user.id
            data = user.data
            getOrCreate = false
        }

    /**
     * Add a User.
     *
     * @param params A function over [UserParams] that define params to be used.
     * @return An [Either] with the [User] or an [StreamError].
     */
    private suspend fun addUser(params: UserParams): Either<StreamError, User> = either {
        params
            .validate()
            .bind()
        userApi.addUser(
            getOrCreate = params.getOrCreate,
            UserRequest(
                id = params.userId,
                data = params.data
            )
        )
            .obtainEntity()
            .map(UserDto::toDomain)
            .bind()
    }

    /**
     * Delete a User by id.
     *
     * @param id The id of the User to be deleted.
     * @return An [Either] with the [Unit] if the reaction was deleted or an [StreamError].
     */
    suspend fun deleteUser(id: String): Either<StreamError, Unit> = either {
        userApi.deleteUser(id)
            .obtainEntity()
            .bind()
    }

    /**
     * Update an already existing user with the given params.
     *
     * @param params A function over [UpdateUserParams] that define params to be used.
     * @return An [Either] with the updated [User] or an [StreamError].
     */
    suspend fun updateUser(params: UpdateUserParams.() -> Unit = {}): Either<StreamError, User> = either {
        val updateUserParams = UpdateUserParams()
            .apply(params)
            .validate()
            .bind()

        userApi.updateUser(
            updateUserParams.userId,
            UpdateUserRequest(data = updateUserParams.data)
        )
            .obtainEntity()
            .map(UserDto::toDomain)
            .bind()
    }
}
