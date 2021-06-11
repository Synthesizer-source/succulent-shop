package school.cactus.succulentshop.utils

import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.ui.product.ProductItem
import school.cactus.succulentshop.ui.product.detail.RelatedProductAdapter
import school.cactus.succulentshop.ui.product.detail.RelatedProductDecoration
import school.cactus.succulentshop.ui.product.list.ProductAdapter
import school.cactus.succulentshop.ui.product.list.ProductDecoration

@BindingAdapter("app:error")
fun TextInputLayout.error(@StringRes errorMessage: Int?) {
    error = errorMessage?.resolveAsString(context)
    isErrorEnabled = errorMessage != null
}

@BindingAdapter("app:listAdapter", "app:products", "app:itemClickListener")
fun RecyclerView.products(
    productAdapter: ProductAdapter,
    products: List<ProductItem>?,
    itemClickListener: (ProductItem) -> Unit
) {
    if (adapter != productAdapter) adapter = productAdapter
    productAdapter.itemClickListener = itemClickListener
    if (itemDecorationCount == 0) addItemDecoration(ProductDecoration())
    productAdapter.submitList(products.orEmpty())
}

@BindingAdapter("app:listAdapter", "app:relatedProducts", "app:itemClickListener")
fun RecyclerView.relatedProducts(
    relatedProductAdapter: RelatedProductAdapter,
    products: List<ProductItem>?,
    itemClickListener: (ProductItem) -> Unit
) {
    if (adapter == null) adapter = relatedProductAdapter
    relatedProductAdapter.itemClickListener = itemClickListener
    if (itemDecorationCount == 0) addItemDecoration(RelatedProductDecoration())
    relatedProductAdapter.submitList(products.orEmpty())
}

@BindingAdapter("app:isVisible")
fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:imageUrl")
fun ImageView.imageUrl(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this)
            .load(it)
            .override(512)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}