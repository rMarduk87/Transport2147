package rpt.games.transport2147.utils.data.database.mappers.profiles

import rpt.games.transport2147.utils.data.appmodels.Profile
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.ProfilesModel

class ProfileToProfilesModel : ModelMapper<Profile, ProfilesModel> {
    override val destination: Class<ProfilesModel> = ProfilesModel::class.java

    override fun map(source: Profile): ProfilesModel {
        return ProfilesModel(
            id = source.id,
            name = source.name,
            sheet = source.sheet,
            history = source.history,
            used = source.used,
        )
    }
}