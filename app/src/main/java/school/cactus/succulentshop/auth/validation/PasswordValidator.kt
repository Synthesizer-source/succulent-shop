package school.cactus.succulentshop.auth.validation

import school.cactus.succulentshop.R

class PasswordValidator : Validator() {

    companion object {
        const val MIN_LEN = 7
        const val MAX_LEN = 40
    }

    override fun isValid(field: String): Boolean {
        error = when {
            !isNotEmpty(field) -> R.string.password_is_required
            !isLonger(MIN_LEN, field) -> R.string.password_is_too_short
            !isShorter(MAX_LEN, field) -> R.string.password_is_too_long
            !isPassword(field) -> R.string.password_must_contain_one_digit_one_lowercase_one_uppercase_one_special
            else -> SUCCESS
        }
        return error == SUCCESS
    }

    fun isPassword(field: String) = field.any { it.isDigit() }
            && field.any { it.isLowerCase() }
            && field.any { it.isUpperCase() }
            && field.any { !it.isLetterOrDigit() }
}