package rpt.games.transport2147.utils.data.database.mappers.dictionary

import rpt.games.transport2147.utils.data.appmodels.Dictionary
import rpt.games.transport2147.utils.data.database.mappers.ModelMapper
import rpt.games.transport2147.utils.data.database.models.DictionaryModel

class DictionaryModelToDictionary : ModelMapper<DictionaryModel, Dictionary> {
    override val destination: Class<Dictionary> = Dictionary::class.java

    override fun map(source: DictionaryModel): Dictionary {
        return Dictionary(
            id = source.id,
            entry = source.entry
        )
    }
}