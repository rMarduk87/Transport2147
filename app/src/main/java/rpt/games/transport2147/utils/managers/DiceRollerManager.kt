package rpt.games.transport2147.utils.managers

import android.view.MenuItem
import androidx.core.view.MotionEventCompat
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.AppUtils


class DiceRollerManager(menuItem: MenuItem?, i: Int) {
    var dices: Int = 1
    private var _menuItem: MenuItem? = null
    var isUsed: Boolean = false
        private set

    private fun simulateLevel(i: Int): Int {
        if (i == 100) {
            return R.drawable.ico_action_dice01
        }
        if (i == 200) {
            return R.drawable.ico_action_dice02
        }
        if (i == 300) {
            return R.drawable.ico_action_dice03
        }
        if (i == 400) {
            return R.drawable.ico_action_dice04
        }
        when (i) {
            1 -> return R.drawable.ico_action_number01
            2 -> return R.drawable.ico_action_number02
            3 -> return R.drawable.ico_action_number03
            4 -> return R.drawable.ico_action_number04
            5 -> return R.drawable.ico_action_number05
            6 -> return R.drawable.ico_action_number06
            7 -> return R.drawable.ico_action_number07
            8 -> return R.drawable.ico_action_number08
            9 -> return R.drawable.ico_action_number09
            10 -> return R.drawable.ico_action_number10
            11 -> return R.drawable.ico_action_number11
            MotionEventCompat.AXIS_RX -> return R.drawable.ico_action_number12
            MotionEventCompat.AXIS_RY -> return R.drawable.ico_action_number13
            MotionEventCompat.AXIS_RZ -> return R.drawable.ico_action_number14
            15 -> return R.drawable.ico_action_number15
            16 -> return R.drawable.ico_action_number16
            MotionEventCompat.AXIS_LTRIGGER -> return R.drawable.ico_action_number17
            MotionEventCompat.AXIS_RTRIGGER -> return R.drawable.ico_action_number18
            19 -> return R.drawable.ico_action_number19
            MotionEventCompat.AXIS_RUDDER -> return R.drawable.ico_action_number20
            MotionEventCompat.AXIS_WHEEL -> return R.drawable.ico_action_number21
            MotionEventCompat.AXIS_GAS -> return R.drawable.ico_action_number22
            MotionEventCompat.AXIS_BRAKE -> return R.drawable.ico_action_number23
            MotionEventCompat.AXIS_DISTANCE -> return R.drawable.ico_action_number24
            else -> return 0
        }
    }

    init {
        this.dices = i
        this._menuItem = menuItem
        setUsage(false)
    }

    fun setMenuItem(menuItem: MenuItem?) {
        this._menuItem = menuItem
        setUsage(false)
    }

    fun setUsage(z: Boolean) {
        this.isUsed = z
        if (z) {
            return
        }
        this._menuItem!!.icon!!.level = this.dices * 100
    }

    fun toggleRollDices() {
        setUsage(!this.isUsed)
        if (this.isUsed) {
            this._menuItem!!.icon!!.level = rollD6(this.dices)
        }
    }

    private fun rollD6(i: Int): Int {
        var iRollD6 = 0
        for (i2 in 0..<i) {
            iRollD6 += rollD6()
        }
        return iRollD6
    }

    private fun rollD6(): Int {
        return AppUtils.random(1, 6)
    }
}
