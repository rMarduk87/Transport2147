package rpt.games.transport2147.utils.data.database.mappers.objects

import rpt.games.transport2147.utils.data.appmodels.Objects
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.ObjectsModel

class ObjectsToObjectsModel : ModelMapper<Objects, ObjectsModel> {
    override val destination: Class<ObjectsModel> = ObjectsModel::class.java

    override fun map(source: Objects): ObjectsModel {
        return ObjectsModel(
            id = source.id,
            item = source.item
        )
    }
}