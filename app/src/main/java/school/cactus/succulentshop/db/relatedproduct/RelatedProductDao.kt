package school.cactus.succulentshop.db.relatedproduct

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RelatedProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelatedProduct(relatedProductEntity: RelatedProductEntity)

    @Query("SELECT * FROM related_product WHERE id = :id")
    suspend fun getRelatedProductById(id: Int): RelatedProductEntity?
}