package rpt.games.transport2147.utils.data.database.mappers

import rpt.games.transport2147.utils.data.appmodels.BookVersion
import rpt.games.transport2147.utils.data.appmodels.Profile
import rpt.games.transport2147.utils.data.database.models.BookVersionModel
import rpt.games.transport2147.utils.data.database.models.ProfilesModel

interface ModelMapper<Source, Destination> {
    val destination: Class<BookVersionModel>

    fun map(source: Source): Destination
}