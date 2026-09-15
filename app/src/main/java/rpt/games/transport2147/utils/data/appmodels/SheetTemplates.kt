package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.sheet.SheetTemplatesToSheetTemplatesModel
import rpt.games.transport2147.utils.data.database.models.SheetTemplatesModel
import java.io.Serializable

@Keep
data class SheetTemplates(
    val id: String,
    val sheetTemplate: String
) : AppModel(), Serializable {

    init {
        addMapper(SheetTemplatesToSheetTemplatesModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == SheetTemplatesModel::class.java }.map(this) as T
    }
}