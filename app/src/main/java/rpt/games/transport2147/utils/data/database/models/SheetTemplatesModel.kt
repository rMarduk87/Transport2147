package rpt.games.transport2147.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.sheet.SheetTemplatesModelToSheetTemplates

@Keep
@Entity(tableName = "sheet_templates")
class SheetTemplatesModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "sheet_template")
    val sheetTemplate: String,
) : DbModel() {

    init {
        addMapper(SheetTemplatesModelToSheetTemplates())
    }
}