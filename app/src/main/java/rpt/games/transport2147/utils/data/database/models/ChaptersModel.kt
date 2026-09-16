package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.chapters.ChaptersModelToChapter

@Keep
@Entity(tableName = "chapters")
class ChaptersModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "chapter")
    val chapter: String,
) : DbModel() {

    init {
        addMapper(ChaptersModelToChapter())
    }
}