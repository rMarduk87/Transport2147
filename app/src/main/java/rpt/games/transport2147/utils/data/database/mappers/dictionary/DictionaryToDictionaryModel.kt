package rpt.games.transport2147.utils.data.database.mappers.dictionary

import rpt.games.transport2147.utils.data.appmodels.Dictionary
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.DictionaryModel

class DictionaryToDictionaryModel : ModelMapper<Dictionary, DictionaryModel> {
    override val destination: Class<DictionaryModel> = DictionaryModel::class.java

    override fun map(source: Dictionary): DictionaryModel {
        return DictionaryModel(
            id = source.id,
            entry = source.entry
        )
    }
}