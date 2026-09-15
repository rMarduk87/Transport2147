package rpt.games.transport2147.utils.data.database.mappers.sheet

import rpt.games.transport2147.utils.data.appmodels.SheetTemplates
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.SheetTemplatesModel

class SheetTemplatesModelToSheetTemplates : ModelMapper<SheetTemplatesModel, SheetTemplates> {
    override val destination: Class<SheetTemplates> = SheetTemplates::class.java

    override fun map(source: SheetTemplatesModel): SheetTemplates {
        return SheetTemplates(
            id = source.id,
            sheetTemplate = source.sheetTemplate
        )
    }
}