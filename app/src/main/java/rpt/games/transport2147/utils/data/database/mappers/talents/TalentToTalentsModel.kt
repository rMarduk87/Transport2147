package rpt.games.transport2147.utils.data.database.mappers.talents

import rpt.games.transport2147.utils.data.appmodels.Talent
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.TalentsModel

class TalentToTalentsModel : ModelMapper<Talent, TalentsModel> {
    override val destination: Class<TalentsModel> = TalentsModel::class.java

    override fun map(source: Talent): TalentsModel {
        return TalentsModel(
            id = source.id,
            talent = source.talent
        )
    }
}