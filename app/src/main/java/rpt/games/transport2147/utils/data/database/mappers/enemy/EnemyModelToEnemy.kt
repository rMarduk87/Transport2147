package rpt.games.transport2147.utils.data.database.mappers.enemy

import rpt.games.transport2147.utils.data.appmodels.Enemy
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.EnemyModel

class EnemyModelToEnemy : ModelMapper<EnemyModel, Enemy> {
    override val destination: Class<Enemy> = Enemy::class.java

    override fun map(source: EnemyModel): Enemy {
        return Enemy(
            id = source.id,
            enemy = source.enemy
        )
    }
}