package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.talents.TalentToTalentsModel
import rpt.games.transport2147.utils.data.database.models.TalentsModel
import java.io.Serializable

@Keep
data class Talent(
    val id: String,
    val talent: String
) : AppModel(), Serializable {

    init {
        addMapper(TalentToTalentsModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == TalentsModel::class.java }.map(this) as T
    }
}