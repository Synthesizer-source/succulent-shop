package school.cactus.succulentshop.auth.validation

import school.cactus.succulentshop.R

class UsernameValidator : Validator() {

    companion object {
        const val MIN_LEN = 2
        const val MAX_LEN = 20
    }

    override fun isValid(field: String): Boolean {
        error = when {
            !isNotEmpty(field) -> R.string.username_is_required
            !isUsername(field) -> R.string.username_can_only_include_az09__characters
            !isLonger(MIN_LEN, field) -> R.string.username_is_too_short
            !isShorter(MAX_LEN, field) -> R.string.username_is_too_long
            else -> SUCCESS
        }
        return error == SUCCESS
    }

    fun isUsername(field: String) = field.filter { it.isLowerCase() || it.isDigit() || it == '_' }
        .count() == field.length
}