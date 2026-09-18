package rpt.games.transport2147.utils.data.appmodels.complex

import android.R
import android.widget.LinearLayout
import rpt.games.transport2147.utils.view.chapter.ChapterFormatter


object History {
    private var _historyEnabled = true
    private var _requestedChapter: String? = null
    private val _visitedChapters = java.util.ArrayList<HistoryElement?>()

    fun cleanHistory() {
        History._visitedChapters.clear()
        History._requestedChapter = null
    }

    fun trimHistory(i: Int) {
        val size: Int = History.size
        val i2 = i + 1
        for (i3 in i2..<size) {
            History._visitedChapters.removeAt(i2)
        }
    }

    var requestedChapter: String?
        get() = History._requestedChapter
        set(str) {
            History._requestedChapter = str
        }

    val lastChapter: HistoryElement?
        get() {
            if (History._visitedChapters.isEmpty()) {
                return null
            }
            return History._visitedChapters[History._visitedChapters.size - 1]
        }

    val size: Int
        get() = History._visitedChapters.size

    fun removeLastChapter() {
        if (History.size < 2) {
            return
        }
        History._visitedChapters.removeAt(History.size - 1)
    }

    fun addVisitedChapter(str: String?, str2: String?) {
        History._visitedChapters.add(
            HistoryElement(
                str,
                _historyEnabled,
                str2
            )
        )
    }

    val visitedChapters: ArrayList<HistoryElement?>
        get() = History._visitedChapters

    var historyEnabled: Boolean
        get() = History._historyEnabled
        set(z) {
            History._historyEnabled = z
        }
}
