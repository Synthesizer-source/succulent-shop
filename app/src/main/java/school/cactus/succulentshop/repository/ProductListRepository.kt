package school.cactus.succulentshop.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.db.db
import school.cactus.succulentshop.infra.ErrorHolder.NetworkError
import school.cactus.succulentshop.infra.ErrorHolder.UnExpectedError
import school.cactus.succulentshop.infra.ErrorHolder.UnauthorizedError
import school.cactus.succulentshop.infra.Resource
import school.cactus.succulentshop.ui.product.ProductItem
import school.cactus.succulentshop.ui.product.toProductEntityList
import school.cactus.succulentshop.ui.product.toProductItemList

class ProductListRepository {

    suspend fun fetchProducts(): Flow<Resource<List<ProductItem>>> = flow {
        emit(Resource.Loading())
        val cachedProducts = db.productDao().getAll()
        if (cachedProducts.isNotEmpty()) {
            emit(Resource.Success(cachedProducts.toProductItemList()))
        }
        val response = try {
            api.listAllProducts()
        } catch (ex: Exception) {
            null
        }

        when (response?.code()) {
            null -> emit(Resource.Failure(NetworkError))
            200 -> {
                val productItems = response.body()!!.toProductItemList()
                emit(Resource.Success(productItems))
                db.productDao().insertAll(productItems.toProductEntityList())
            }
            401 -> emit(Resource.Failure(UnauthorizedError))
            else -> emit(Resource.Failure(UnExpectedError))
        }
    }
}