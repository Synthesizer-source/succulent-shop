package school.cactus.succulentshop.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import school.cactus.succulentshop.db.product.ProductDao
import school.cactus.succulentshop.db.product.ProductEntity
import school.cactus.succulentshop.db.relatedproduct.RelatedProductDao
import school.cactus.succulentshop.db.relatedproduct.RelatedProductEntity


@Database(
    entities = [ProductEntity::class, RelatedProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(IntListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun relatedProductDao(): RelatedProductDao
}

class IntListConverter {

    @TypeConverter
    fun fromIntList(list: List<Int>?) = Gson().toJson(list)

    @TypeConverter
    fun toIntList(json: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(json, type)
    }
}