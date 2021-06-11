package school.cactus.succulentshop.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.auth.validation.EmailValidator
import school.cactus.succulentshop.auth.validation.PasswordValidator
import school.cactus.succulentshop.auth.validation.UsernameValidator
import school.cactus.succulentshop.auth.validation.Validator
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.ErrorHolder.ClientError
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.repository.SignUpRepository

class SignUpViewModel(
    private val store: JwtStore,
    private val repository: SignUpRepository
) : BaseViewModel() {

    private val emailValidator = EmailValidator()
    private val usernameValidator = UsernameValidator()
    private val passwordValidator = PasswordValidator()

    val email = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _emailErrorMessage = MutableLiveData<Int?>()
    val emailErrorMessage: LiveData<Int?> = _emailErrorMessage

    private val _usernameErrorMessage = MutableLiveData<Int?>()
    val usernameErrorMessage: LiveData<Int?> = _usernameErrorMessage

    private val _passwordErrorMessage = MutableLiveData<Int?>()
    val passwordErrorMessage: LiveData<Int?> = _passwordErrorMessage

    private val _keyboardFlag = MutableLiveData<Int>()
    val keyboardFlag: LiveData<Int> = _keyboardFlag

    fun onSignUpButtonClick() = viewModelScope.launch {
        hideKeyboard()
        if (isEmailValid() and isUsernameValid() and isPasswordValid()) {
            val response = repository.sendSignUpRequest(
                email = email.value.orEmpty(),
                username = username.value.orEmpty(),
                password = password.value.orEmpty()
            )

            if (response is Resource.Success) onSuccess(response.data)
            else {
                when (val error = (response as Resource.Failure).errorHolder) {
                    NetworkError -> onNetworkError()
                    is ClientError -> onClientError(error.message)
                    else -> onUnexpectedError()
                }
            }
        }
    }

    private fun onSuccess(jwt: String) {
        store.saveJwt(jwt)
        navigateToProductList()
    }

    private fun onClientError(errorMessage: String) {
        _snackbarState.value = SnackbarState(
            error = errorMessage,
            duration = Snackbar.LENGTH_LONG
        )
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = Snackbar.LENGTH_LONG
        )
    }

    private fun onNetworkError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    onSignUpButtonClick()
                }
            )
        )
    }

    private fun hideKeyboard() {
        _keyboardFlag.value = 0
    }

    private fun isValid(validator: Validator, field: String?, errorMessage: MutableLiveData<Int?>) =
        validator.isValid(field.orEmpty()).also {
            errorMessage.value = if (it) null else validator.getError()
        }

    private fun isEmailValid() =
        isValid(emailValidator, email.value, _emailErrorMessage)

    private fun isUsernameValid() =
        isValid(usernameValidator, username.value, _usernameErrorMessage)

    private fun isPasswordValid() =
        isValid(passwordValidator, password.value, _passwordErrorMessage)

    private fun navigateToProductList() {
        val action = SignUpFragmentDirections.signUpSuccess()
        navigation.navigate(action)
    }

    fun onAlreadyHaveAnAccountButtonClick() {
        val action = SignUpFragmentDirections.login()
        navigation.navigate(action)
    }
}