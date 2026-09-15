package rpt.games.transport2147.utils.data.database.mappers.talents

import rpt.games.transport2147.utils.data.appmodels.Talent
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.TalentsModel

class TalentsModelToTalent : ModelMapper<TalentsModel, Talent> {
    override val destination: Class<Talent> = Talent::class.java

    override fun map(source: TalentsModel): Talent {
        return Talent(
            id = source.id,
            talent = source.talent
        )
    }
}