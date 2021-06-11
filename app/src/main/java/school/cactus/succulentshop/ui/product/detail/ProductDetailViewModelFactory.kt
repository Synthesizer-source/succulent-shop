package school.cactus.succulentshop.ui.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.repository.ProductDetailRepository

@Suppress("UNCHECKED_CAST")
class ProductDetailViewModelFactory(
    private val productId: Int,
    private val store: JwtStore,
    private val repository: ProductDetailRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        ProductDetailViewModel(productId, store, repository) as T
}