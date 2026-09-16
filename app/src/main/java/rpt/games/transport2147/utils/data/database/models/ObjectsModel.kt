package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.objects.ObjectsModelToObjects

@Keep
@Entity(tableName = "objects")
class ObjectsModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "item")
    val item: String,
) : DbModel() {

    init {
        addMapper(ObjectsModelToObjects())
    }
}