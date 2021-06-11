package school.cactus.succulentshop.ui.product.detail


import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import school.cactus.succulentshop.R

class RelatedProductDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacing =
            view.context.resources.getDimensionPixelSize(R.dimen.related_product_horizontal_spacing)
        val position = parent.getChildAdapterPosition(view)
        if (position < parent.adapter!!.itemCount - 1) outRect.right = spacing
    }
}