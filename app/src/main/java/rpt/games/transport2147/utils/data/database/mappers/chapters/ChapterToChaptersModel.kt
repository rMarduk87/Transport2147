package rpt.games.transport2147.utils.data.database.mappers.chapters

import rpt.games.transport2147.utils.data.appmodels.Chapter
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.ChaptersModel

class ChapterToChaptersModel : ModelMapper<Chapter, ChaptersModel> {
    override val destination: Class<ChaptersModel> = ChaptersModel::class.java

    override fun map(source: Chapter): ChaptersModel {
        return ChaptersModel(
            id = source.id,
            chapter = source.chapter,
        )
    }
}