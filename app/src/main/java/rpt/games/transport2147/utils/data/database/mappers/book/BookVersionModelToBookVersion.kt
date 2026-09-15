package rpt.games.transport2147.utils.data.database.mappers.book

import rpt.games.transport2147.utils.data.appmodels.BookVersion
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.BookVersionModel

class BookVersionModelToBookVersion  : ModelMapper<BookVersionModel,BookVersion> {
    override val destination: Class<BookVersion> = BookVersion::class.java

    override fun map(source: BookVersionModel): BookVersion {
        return BookVersion(
            id = source.id,
            version = source.version,
            language = source.language
        )
    }
}