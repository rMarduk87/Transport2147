package rpt.games.transport2147.utils.data.database.mappers.chapters

import rpt.games.transport2147.utils.data.appmodels.Chapter
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.ChaptersModel


class ChaptersModelToChapter : ModelMapper<ChaptersModel, Chapter> {
    override val destination: Class<Chapter> = Chapter::class.java

    override fun map(source: ChaptersModel): Chapter {
        return Chapter(
            id = source.id,
            chapter = source.chapter
        )
    }
}