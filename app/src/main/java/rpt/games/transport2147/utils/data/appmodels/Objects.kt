package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.objects.ObjectsToObjectsModel
import rpt.games.transport2147.utils.data.database.models.ObjectsModel
import java.io.Serializable

@Keep
data class Objects(
    val id: String,
    val item: String
) : AppModel(), Serializable {

    init {
        addMapper(ObjectsToObjectsModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == ObjectsModel::class.java }.map(this) as T
    }
}