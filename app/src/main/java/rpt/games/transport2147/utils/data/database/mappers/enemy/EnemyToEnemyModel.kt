package rpt.games.transport2147.utils.data.database.mappers.enemy

import rpt.games.transport2147.utils.data.appmodels.Enemy
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.EnemyModel

class EnemyToEnemyModel : ModelMapper<Enemy, EnemyModel> {
    override val destination: Class<EnemyModel> = EnemyModel::class.java

    override fun map(source: Enemy): EnemyModel {
        return source.id?.let {
            source.enemy?.let { enemy ->
                EnemyModel(
                    id = it,
                    enemy = enemy
                )
            }
        }!!
    }
}