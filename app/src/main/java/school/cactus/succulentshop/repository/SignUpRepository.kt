package school.cactus.succulentshop.repository

import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.signup.SignUpRequest
import school.cactus.succulentshop.infra.ErrorHolder.ClientError
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.ErrorHolder.UnExpectedError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.utils.errorMessage

class SignUpRepository {

    suspend fun sendSignUpRequest(
        email: String,
        username: String,
        password: String
    ): Resource<String> {
        val response = try {
            api.signUp(SignUpRequest(email = email, username = username, password = password))
        } catch (exception: Exception) {
            null
        }

        return when (response?.code()) {
            200 -> Resource.Success(response.body()!!.jwt)
            null -> Resource.Failure(NetworkError)
            in 400..499 -> Resource.Failure(ClientError(response.errorBody()!!.errorMessage()))
            else -> Resource.Failure(UnExpectedError)
        }
    }
}