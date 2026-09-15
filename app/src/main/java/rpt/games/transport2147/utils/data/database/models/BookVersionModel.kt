package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.book.BookVersionModelToBookVersion
import rpt.games.transport2147.utils.data.database.mappers.dictionary.DictionaryModelToDictionary

@Keep
@Entity(tableName = "book_version")
class BookVersionModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "version")
    val version: Int,
    @ColumnInfo(name = "language")
    val language: String,
) : DbModel() {

    init {
        addMapper(BookVersionModelToBookVersion())
    }
}