package rpt.games.transport2147.utils.view.game

import android.util.SparseArray
import rpt.games.transport2147.R


enum class DiceRollerDispatcher(val num: Int, val id: Int) {
    D1(1, R.id.dlgChangeDices_rdb1),
    D2(2, R.id.dlgChangeDices_rdb2),
    D3(3, R.id.dlgChangeDices_rdb3),
    D4(4, R.id.dlgChangeDices_rdb4);

    companion object {
        private val lookup = SparseArray<DiceRollerDispatcher?>()
        private val lookup2 = SparseArray<DiceRollerDispatcher?>()

        init {
            for (diceRollerDispatcher in entries) {
                lookup.put(diceRollerDispatcher.num, diceRollerDispatcher)
                lookup2.put(diceRollerDispatcher.id, diceRollerDispatcher)
            }
        }

        fun getFromNum(i: Int): DiceRollerDispatcher? {
            return lookup.get(i)
        }

        fun getFromId(i: Int): DiceRollerDispatcher? {
            return lookup2.get(i)
        }
    }
}