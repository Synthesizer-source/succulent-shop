package school.cactus.succulentshop.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.db.db
import school.cactus.succulentshop.db.relatedproduct.RelatedProductEntity
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.ErrorHolder.UnExpectedError
import school.cactus.succulentshop.infra.ErrorHolder.UnauthorizedError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.ui.product.ProductItem
import school.cactus.succulentshop.ui.product.toProductItem
import school.cactus.succulentshop.ui.product.toProductItemList

class ProductDetailRepository {
    suspend fun fetchProductDetail(productId: Int): Flow<Resource<ProductItem>> = flow {
        emit(Resource.Loading())
        val cachedProduct = db.productDao().getById(productId)
        if (cachedProduct == null) {
            val response = try {
                api.getProductById(productId)
            } catch (ex: Exception) {
                null
            }

            emit(
                when (response?.code()) {
                    null -> Resource.Failure(NetworkError)
                    200 -> Resource.Success(response.body()!!.toProductItem())
                    401 -> Resource.Failure(UnauthorizedError)
                    else -> Resource.Failure(UnExpectedError)
                }
            )
        } else {
            emit(Resource.Success(cachedProduct.toProductItem()))
        }
    }

    suspend fun fetchRelatedProducts(productId: Int): Flow<Resource<List<ProductItem>>> = flow {
        val cachedRelatedProduct = db.relatedProductDao().getRelatedProductById(productId)
        if (cachedRelatedProduct != null) {
            val relatedProducts = cachedRelatedProduct.relatedProductIds.map {
                db.productDao().getById(it)!!.toProductItem()
            }
            emit(Resource.Success(relatedProducts))
        }

        val response = try {
            api.getRelatedProductsById(productId)
        } catch (ex: Exception) {
            null
        }

        when (response?.code()) {
            null -> emit(Resource.Failure(NetworkError))
            200 -> {
                val relatedProducts = response.body()!!.products.toProductItemList()
                val relatedProductIds = relatedProducts.map { it.id }
                db.relatedProductDao()
                    .insertRelatedProduct(RelatedProductEntity(productId, relatedProductIds))
                emit(Resource.Success(relatedProducts))
            }
            401 -> emit(Resource.Failure(UnauthorizedError))
            else -> emit(Resource.Failure(UnExpectedError))
        }
    }
}