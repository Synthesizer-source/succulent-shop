package school.cactus.succulentshop.ui.product.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ProductImageViewModelFactory(private val imageUrl: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        ProductImageViewModel(imageUrl) as T
}