package rpt.games.transport2147.utils.managers

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rpt.games.transport2147.TransportApplication
import rpt.games.transport2147.utils.data.database.AppDatabase
import rpt.games.transport2147.utils.data.repositories.BookRepository

object RepositoryManager {

    private val ctx: Context
        get() = TransportApplication.instance

    private val db by lazy { AppDatabase(ctx) }

    val bookRepository: BookRepository by lazy {
        BookRepository(db.transportDao())
    }


    suspend fun clear() {
        withContext(Dispatchers.Default) {
            bookRepository.clearAll()
        }
    }
}