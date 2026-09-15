package rpt.games.transport2147.utils.data.appmodels

import androidx.annotation.Keep
import rpt.games.transport2147.utils.data.AppModel
import rpt.games.transport2147.utils.data.DbModel
import rpt.games.transport2147.utils.data.database.mappers.addMapper
import rpt.games.transport2147.utils.data.database.mappers.dictionary.DictionaryToDictionaryModel
import rpt.games.transport2147.utils.data.database.models.DictionaryModel
import java.io.Serializable

@Keep
data class Dictionary(
    val id: String,
    val entry: String
) : AppModel(), Serializable {

    init {
        addMapper(DictionaryToDictionaryModel())
    }

    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == DictionaryModel::class.java }.map(this) as T
    }
}