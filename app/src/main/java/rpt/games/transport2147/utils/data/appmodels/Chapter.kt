package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.chapters.ChapterToChaptersModel
import rpt.games.transport2147.utils.data.database.models.ChaptersModel
import java.io.Serializable

@Keep
data class Chapter(
    val id: Int,
    val chapter: String
) : AppModel(), Serializable {

    init {
        addMapper(ChapterToChaptersModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == ChaptersModel::class.java }.map(this) as T
    }
}