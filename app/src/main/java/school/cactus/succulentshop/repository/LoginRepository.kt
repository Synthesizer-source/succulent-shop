package school.cactus.succulentshop.repository

import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.infra.ErrorHolder.ClientError
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.ErrorHolder.UnExpectedError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.utils.errorMessage

class LoginRepository {
    suspend fun sendLoginRequest(
        identifier: String,
        password: String
    ): Resource<String> {
        val request = LoginRequest(identifier, password)

        val response = try {
            api.login(request)
        } catch (ex: Exception) {
            null
        }

        return when (response?.code()) {
            null -> Resource.Failure(NetworkError)
            200 -> Resource.Success(response.body()!!.jwt)
            in 400..499 -> Resource.Failure(ClientError(response.errorBody()!!.errorMessage()))
            else -> Resource.Failure(UnExpectedError)
        }
    }
}