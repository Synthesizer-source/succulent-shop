package school.cactus.succulentshop.ui.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.repository.ProductListRepository

@Suppress("UNCHECKED_CAST")
class ProductListViewModelFactory(
    private val store: JwtStore,
    private val repository: ProductListRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        ProductListViewModel(store, repository) as T
}