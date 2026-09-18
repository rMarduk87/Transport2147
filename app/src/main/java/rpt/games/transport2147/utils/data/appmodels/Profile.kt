package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.profiles.ProfileToProfilesModel
import rpt.games.transport2147.utils.data.database.models.ProfilesModel
import java.io.Serializable

@Keep
data class Profile(
    val id: String,
    var name: String,
    val sheet: String,
    val history: String,
    val used: String
) : AppModel(), Serializable {

    init {
        addMapper(ProfileToProfilesModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == ProfilesModel::class.java }.map(this) as T
    }
}