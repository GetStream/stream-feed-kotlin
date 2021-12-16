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

class UserClient internal constructor(private val userApi: UserApi) {

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

    suspend fun getOrCreateUser(params: UserParams.() -> Unit = {}): Either<StreamError, User> = addUser(UserParams().apply(params))
    suspend fun createUser(params: UserParams.() -> Unit = {}): Either<StreamError, User> = addUser(UserParams().apply(params))

    suspend fun createUser(user: User): Either<StreamError, User> =
        createUser {
            userId = user.id
            data = user.data
            getOrCreate = false
        }

    suspend fun addUser(params: UserParams): Either<StreamError, User> = either {
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

    suspend fun deleteUser(id: String): Either<StreamError, Unit> = either {
        userApi.deleteUser(id)
            .obtainEntity()
            .bind()
    }

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
