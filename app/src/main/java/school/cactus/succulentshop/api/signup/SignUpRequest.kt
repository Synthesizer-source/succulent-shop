package school.cactus.succulentshop.api.signup

data class SignUpRequest(
    val email: String,
    val password: String,
    val username: String
)