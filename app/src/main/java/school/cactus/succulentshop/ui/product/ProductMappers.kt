package school.cactus.succulentshop.ui.product

import school.cactus.succulentshop.api.BASE_URL
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.db.product.ProductEntity
import school.cactus.succulentshop.utils.formatPrice

fun List<Product>.toProductItemList() = map {
    it.toProductItem()
}

fun Product.toProductItem() = ProductItem(
    id = id,
    title = title,
    description = description,
    price = price.formatPrice(),
    imageUrl = BASE_URL + image.formats.small.url,
    highResImageUrl = BASE_URL + image.formats.medium.url
)

fun List<ProductItem>.toProductEntityList() = map {
    it.toProductEntity()
}

fun ProductItem.toProductEntity() = ProductEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    imageUrl = imageUrl,
    highResImageUrl = highResImageUrl
)

@JvmName("toProductItemListProductEntity")
fun List<ProductEntity>.toProductItemList() = map {
    it.toProductItem()
}

fun ProductEntity.toProductItem() = ProductItem(
    id = id,
    title = title,
    description = description,
    price = price,
    imageUrl = imageUrl,
    highResImageUrl = highResImageUrl
)