package rpt.games.transport2147.utils.data.database.mappers.objects

import rpt.games.transport2147.utils.data.appmodels.Objects
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.ObjectsModel

class ObjectsModelToObjects : ModelMapper<ObjectsModel, Objects> {
    override val destination: Class<Objects> = Objects::class.java

    override fun map(source: ObjectsModel): Objects {
        return Objects(
            id = source.id,
            item = source.item
        )
    }
}