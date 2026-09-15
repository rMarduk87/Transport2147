package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.dictionary.DictionaryModelToDictionary

@Keep
@Entity(tableName = "dictionary")
class DictionaryModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "entry")
    val entry: String,
) : DbModel() {

    init {
        addMapper(DictionaryModelToDictionary())
    }
}