package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.book.BookVersionToBookVersionModel
import rpt.games.transport2147.utils.data.database.mappers.chapters.ChapterToChaptersModel
import rpt.games.transport2147.utils.data.database.models.BookVersionModel
import java.io.Serializable

@Keep
data class BookVersion(
    val id: Int,
    val language: String,
    val version: Int
) : AppModel(), Serializable {

    init {
        addMapper(BookVersionToBookVersionModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == BookVersionModel::class.java }.map(this) as T
    }
}