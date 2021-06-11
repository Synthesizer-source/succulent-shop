package school.cactus.succulentshop.auth.validation

abstract class Validator {

    companion object {
        const val SUCCESS = -1
    }

    protected var error: Int? = SUCCESS

    abstract fun isValid(field: String): Boolean

    fun isNotEmpty(field: String) = field.isNotEmpty()

    fun isLonger(minLen: Int, field: String) = field.length > minLen

    fun isShorter(maxLen: Int, field: String) = field.length < maxLen

    fun getError() = error!!
}