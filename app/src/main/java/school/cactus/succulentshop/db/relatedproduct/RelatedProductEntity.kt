package school.cactus.succulentshop.db.relatedproduct

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "related_product")
data class RelatedProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "related_ids") val relatedProductIds: List<Int>
)