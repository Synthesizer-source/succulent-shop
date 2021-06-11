package school.cactus.succulentshop.ui.product.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import school.cactus.succulentshop.databinding.ItemRelatedProductBinding
import school.cactus.succulentshop.ui.product.ProductItem
import school.cactus.succulentshop.ui.product.detail.RelatedProductAdapter.RelatedHolder

class RelatedProductAdapter : ListAdapter<ProductItem, RelatedHolder>(DIFF_CALLBACK) {

    var itemClickListener: (ProductItem) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedHolder {
        val binding = ItemRelatedProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RelatedHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RelatedHolder, position: Int) =
        holder.bind(getItem(position))

    class RelatedHolder(
        private val binding: ItemRelatedProductBinding,
        private val itemClickListener: (ProductItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductItem) {
            binding.apply {
                item = product
                root.setOnClickListener {
                    itemClickListener(product)
                }
                executePendingBindings()
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem) =
                oldItem == newItem
        }
    }
}