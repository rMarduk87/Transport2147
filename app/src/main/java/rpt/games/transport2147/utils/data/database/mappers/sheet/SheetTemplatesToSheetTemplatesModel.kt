package rpt.games.transport2147.utils.data.database.mappers.sheet

import rpt.games.transport2147.utils.data.appmodels.SheetTemplates
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.SheetTemplatesModel

class SheetTemplatesToSheetTemplatesModel : ModelMapper<SheetTemplates, SheetTemplatesModel> {
    override val destination: Class<SheetTemplatesModel> = SheetTemplatesModel::class.java

    override fun map(source: SheetTemplates): SheetTemplatesModel {
        return SheetTemplatesModel(
            id = source.id,
            sheetTemplate = source.sheetTemplate
        )
    }
}