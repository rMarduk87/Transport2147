package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.profiles.ProfilesModelToProfile

@Keep
@Entity(tableName = "profiles")
class ProfilesModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "sheet")
    val sheet: String,
    @ColumnInfo(name = "history")
    val history: String,
    @ColumnInfo(name = "used")
    val used: String,
) : DbModel() {

    init {
        addMapper(ProfilesModelToProfile())
    }
}