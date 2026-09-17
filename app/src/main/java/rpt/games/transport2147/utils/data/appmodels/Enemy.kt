package rpt.games.transport2147.utils.data.appmodels

import android.content.Context
import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.enemy.EnemyToEnemyModel
import rpt.games.transport2147.utils.data.database.models.EnemyModel
import java.io.Serializable

@Keep
data class Enemy(
    val id: String?,
    val enemy: String?
) : AppModel(), Serializable {

    init {
        addMapper(EnemyToEnemyModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == EnemyModel::class.java }.map(this) as T
    }
}