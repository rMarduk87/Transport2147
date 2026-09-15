package rpt.games.transport2147.utils.data.database.mappers.profiles

import rpt.games.transport2147.utils.data.appmodels.Profile
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.ProfilesModel

class ProfilesModelToProfile : ModelMapper<ProfilesModel, Profile> {
    override val destination: Class<Profile> = Profile::class.java

    override fun map(source: ProfilesModel): Profile {
        return Profile(
            id = source.id,
            name = source.name,
            sheet = source.sheet,
            history = source.history,
            used = source.used,
        )
    }
}