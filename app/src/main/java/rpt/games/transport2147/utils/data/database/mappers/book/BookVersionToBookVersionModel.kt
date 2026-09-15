package rpt.games.transport2147.utils.data.database.mappers.book

import rpt.games.transport2147.utils.data.appmodels.BookVersion
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.BookVersionModel

class BookVersionToBookVersionModel: ModelMapper<BookVersion, BookVersionModel> {
    override val destination: Class<BookVersionModel> = BookVersionModel::class.java

    override fun map(source: BookVersion): BookVersionModel {
        return BookVersionModel(
            id = source.id,
            version = source.version,
            language = source.language
        )
    }
}