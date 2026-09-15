package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper

@Keep
@Entity(tableName = "enemies")
class EnemyModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "enemy")
    val enemy: String,
) : DbModel() {

    init {
        addMapper(EnemyModelToEnemy())
    }
}