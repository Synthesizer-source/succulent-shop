package school.cactus.succulentshop.ui.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.ErrorHolder.UnauthorizedError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.repository.ProductListRepository
import school.cactus.succulentshop.ui.product.ProductItem

class ProductListViewModel(
    private val store: JwtStore,
    private val repository: ProductListRepository
) : BaseViewModel() {

    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>> = _products

    private var _progressIndicatorVisibility = MutableLiveData<Boolean>()
    val progressIndicatorVisibility: LiveData<Boolean> = _progressIndicatorVisibility

    private var _productListVisibility = MutableLiveData<Boolean>()
    val productListVisibility: LiveData<Boolean> = _productListVisibility

    val itemClickListener: (ProductItem) -> Unit = {
        navigateToProductDetail(it.id)
    }

    init {
        if (store.loadJwt() == null) navigateToLogin()
        fetchProducts()
    }

    private fun fetchProducts() = viewModelScope.launch {
        repository.fetchProducts().collect {
            when (it) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(it.data)
                is Resource.Failure -> {
                    when (it.errorHolder) {
                        NetworkError -> onNetworkError()
                        UnauthorizedError -> onTokenExpired()
                        else -> onUnexpectedError()
                    }
                }
            }
        }
    }

    private fun onLoading() {
        _productListVisibility.value = false
        _progressIndicatorVisibility.value = true
    }

    private fun onSuccess(products: List<ProductItem>) {
        _products.value = products
        _progressIndicatorVisibility.value = false
        _productListVisibility.value = true
        hideSnackbar()
    }

    private fun onTokenExpired() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.your_session_is_expired,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.log_in,
                action = {
                    logOut()
                }
            )
        )
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = Snackbar.LENGTH_LONG,
        )
    }

    private fun onNetworkError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    fetchProducts()
                }
            )
        )
    }

    private fun navigateToLogin() {
        val action = ProductListFragmentDirections.tokenExpired()
        navigation.navigate(action)
    }

    private fun navigateToProductDetail(productId: Int) {
        val action = ProductListFragmentDirections.showProductDetail(productId)
        navigation.navigate(action)
    }

    fun logOut() {
        store.deleteJwt()
        navigateToLogin()
    }
}