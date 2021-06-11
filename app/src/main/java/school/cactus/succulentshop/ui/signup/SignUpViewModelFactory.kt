package school.cactus.succulentshop.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.repository.SignUpRepository

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(
    private val store: JwtStore,
    private val repository: SignUpRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        SignUpViewModel(store, repository) as T
}