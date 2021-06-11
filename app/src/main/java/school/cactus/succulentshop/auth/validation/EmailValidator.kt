package school.cactus.succulentshop.auth.validation

import school.cactus.succulentshop.R

class EmailValidator : Validator() {

    companion object {
        const val MIN_LEN = 5
        const val MAX_LEN = 50
    }

    override fun isValid(field: String): Boolean {
        error = when {
            !isNotEmpty(field) -> R.string.email_is_required
            !isEmail(field)
                    || !isLonger(MIN_LEN, field)
                    || !isShorter(MAX_LEN, field) -> R.string.email_is_invalid
            else -> SUCCESS
        }
        return error == SUCCESS
    }

    fun isEmail(field: String) = '@' in field && '.' in field
}