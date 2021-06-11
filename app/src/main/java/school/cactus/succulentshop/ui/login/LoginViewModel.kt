package school.cactus.succulentshop.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.auth.validation.IdentifierValidator
import school.cactus.succulentshop.auth.validation.PasswordValidator
import school.cactus.succulentshop.auth.validation.Validator
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.ErrorHolder.ClientError
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.repository.LoginRepository

class LoginViewModel(
    private val store: JwtStore,
    private val repository: LoginRepository
) : BaseViewModel() {

    private val identifierValidator = IdentifierValidator()
    private val passwordValidator = PasswordValidator()

    val identifier = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _identifierErrorMessage = MutableLiveData<Int?>()
    val identifierErrorMessage: LiveData<Int?> = _identifierErrorMessage

    private val _passwordErrorMessage = MutableLiveData<Int?>()
    val passwordErrorMessage: LiveData<Int?> = _passwordErrorMessage

    private val _keyboardFlag = MutableLiveData<Int>()
    val keyboardFlag: LiveData<Int> = _keyboardFlag

    init {
        if (store.loadJwt() != null) navigateToProductList()
    }

    fun onLoginButtonClick() = viewModelScope.launch {
        hideKeyboard()
        if (isIdentifierValid() and isPasswordValid()) {
            val response =
                repository.sendLoginRequest(identifier.value.orEmpty(), password.value.orEmpty())

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
            duration = LENGTH_LONG
        )
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = LENGTH_LONG
        )
    }

    private fun onNetworkError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    onLoginButtonClick()
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

    private fun isIdentifierValid() =
        isValid(identifierValidator, identifier.value, _identifierErrorMessage)

    private fun isPasswordValid() =
        isValid(passwordValidator, password.value, _passwordErrorMessage)

    private fun navigateToProductList() {
        val action = LoginFragmentDirections.loginSuccessful()
        navigation.navigate(action)
    }

    fun onCreateAnAccountButtonClick() {
        val action = LoginFragmentDirections.createAccount()
        navigation.navigate(action)
    }
}