package school.cactus.succulentshop.ui.product.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.async
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
import school.cactus.succulentshop.repository.ProductDetailRepository
import school.cactus.succulentshop.ui.product.ProductItem

class ProductDetailViewModel(
    private val productId: Int,
    private val store: JwtStore,
    private val repository: ProductDetailRepository
) : BaseViewModel() {

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product

    private val _relatedProducts = MutableLiveData<List<ProductItem>>()
    val relatedProducts: LiveData<List<ProductItem>> = _relatedProducts

    private val _progressIndicatorVisibility = MutableLiveData<Boolean>()
    val progressIndicatorVisibility: LiveData<Boolean> = _progressIndicatorVisibility

    private val _productDetailVisibility = MutableLiveData<Boolean>()
    val productDetailVisibility: LiveData<Boolean> = _productDetailVisibility

    private val _relatedProductVisibility = MutableLiveData<Boolean>()
    val relatedProductVisibility: LiveData<Boolean> = _relatedProductVisibility

    val itemClickListener: (ProductItem) -> Unit = {
        navigateToRelatedProduct(it.id)
    }

    init {
        fetchProduct()
    }

    private fun fetchProduct() = viewModelScope.launch {
        val defferedProductDetail = async { repository.fetchProductDetail(productId) }
        val defferedRelatedProduct = async { repository.fetchRelatedProducts(productId) }

        val responseProductDetail = defferedProductDetail.await()
        val responseRelatedProduct = defferedRelatedProduct.await()

        responseProductDetail.collect {
            when (it) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccessProductDetail(it.data)
                is Resource.Failure -> {
                    when (it.errorHolder) {
                        NetworkError -> onNetworkError()
                        UnauthorizedError -> onTokenExpired()
                        else -> onUnexpectedError()
                    }
                }
            }
        }

        responseRelatedProduct.collect {
            when (it) {
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccessRelatedProduct(it.data)
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
        _productDetailVisibility.value = false
        _relatedProductVisibility.value = false
        _progressIndicatorVisibility.value = true
    }

    private fun onSuccessProductDetail(product: ProductItem) {
        _product.value = product
        _progressIndicatorVisibility.value = false
        _productDetailVisibility.value = true
    }

    private fun onSuccessRelatedProduct(relatedProducts: List<ProductItem>) {
        _relatedProducts.value = relatedProducts
        if (relatedProducts.isNotEmpty()) _relatedProductVisibility.value = true
        hideSnackbar()
    }

    private fun onTokenExpired() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.your_session_is_expired,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.log_in,
                action = {
                    store.deleteJwt()
                    navigateToLogin()
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
                    fetchProduct()
                }
            )
        )
    }

    private fun navigateToLogin() {
        val action = ProductDetailFragmentDirections.tokenExpired()
        navigation.navigate(action)
    }

    private fun navigateToRelatedProduct(relatedProductId: Int) {
        val action = ProductDetailFragmentDirections.showRelatedProduct(relatedProductId)
        navigation.navigate(action)
    }

    fun navigateToProductImage() {
        val action =
            ProductDetailFragmentDirections.showProductImage(product.value!!.highResImageUrl)
        navigation.navigate(action)
    }
}