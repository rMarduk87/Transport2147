package rpt.games.transport2147.utils.managers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.TextView
import rpt.com.base.log.e
import rpt.games.transport2147.NavigatorActivity
import rpt.games.transport2147.utils.data.appmodels.complex.BaseValue
import rpt.games.transport2147.utils.data.appmodels.complex.Enemy
import rpt.games.transport2147.utils.data.enums.AbilityTypeEnum
import rpt.games.transport2147.utils.data.enums.BaseValueModeEnum
import rpt.games.transport2147.utils.view.game.GameLogic
import kotlin.math.max
import kotlin.math.min
import rpt.games.transport2147.R
import rpt.games.transport2147.TutorialActivity
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.view.game.PlayerSheet


object CombatManager {
    private const val MAXVAL = 99
    private const val MINVAL = 0
    private var _needValueNotTrasferredAlert = false
    private var _playerCraft = 0
    private var _playerDamage = 0
    private var _playerME = 0
    private var _playerPE = 0
    private var _playerProtection = 0
    private var _playerValor = 0
    private var _round = 0

    fun activateEnemy(context: Context, enemy: Enemy?) {
        try {
            val playerSheet: PlayerSheet? = GameLogic.playerSheet
            _playerValor = playerSheet!!.getAbility(AbilityTypeEnum.VALOR)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value
            _playerCraft = playerSheet.getAbility(AbilityTypeEnum.CRAFT)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value
            _playerDamage = playerSheet.getAbility(AbilityTypeEnum.DAMAGE)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value
            _playerProtection = playerSheet.getAbility(AbilityTypeEnum.PROTECTION)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value
            _playerPE = playerSheet.getAbility(AbilityTypeEnum.PHYSICAL_ENERGY)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value
            _playerME = playerSheet.getAbility(AbilityTypeEnum.MENTAL_ENERGY)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value
            _round = 1
            GameLogic.navigator!!.changePagerPage(2)
            GameLogic.enemy = enemy
            initCombat(true)
            _needValueNotTrasferredAlert = true
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun checkForExitAlert(context: Context?) {
        if (_needValueNotTrasferredAlert) {
            DialogManager.showCombatExitAlert(context!!)
            _needValueNotTrasferredAlert = false
        }
    }

    fun terminateCombat() {
        try {
            GameLogic.enemy = null
            val playerSheet: PlayerSheet? = GameLogic.playerSheet
            val baseValue: BaseValue = playerSheet!!.getAbility(AbilityTypeEnum.MENTAL_ENERGY)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!
            for (value in baseValue.value..<_playerME) {
                baseValue.add()
            }
            for (value2 in baseValue.value downTo _playerME + 1) {
                baseValue.subtract()
            }
            val baseValue2: BaseValue = playerSheet.getAbility(AbilityTypeEnum.PHYSICAL_ENERGY)!!
                .getBaseValue(BaseValueModeEnum.ACTUAL)!!
            for (value3 in baseValue2.value..<_playerPE) {
                baseValue2.add()
            }
            for (value4 in baseValue2.value downTo _playerPE + 1) {
                baseValue2.subtract()
            }
            _needValueNotTrasferredAlert = false
            GameLogic.navigator!!.changePagerPage(1)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    private fun toggleActivation(z: Boolean) {
        val activity_Navigator: NavigatorActivity? = GameLogic.navigator
        activity_Navigator?.findViewById<View>(R.id.frgCombat_layCombat)?.visibility = if (z)
            View.VISIBLE else View.GONE
        activity_Navigator?.findViewById<View>(R.id.frgCombat_lblInactive)?.visibility = if (z)
            View.GONE else View.VISIBLE
    }

    private fun getNormalizedValue(i: Int): Int {
        return max(min(i, MAXVAL), 0)
    }

    fun initCombat(bool: Boolean) {
        val activity_Navigator: NavigatorActivity = GameLogic.navigator ?: return
        val playerSheet: PlayerSheet? = GameLogic.playerSheet
        val enemy: Enemy? = GameLogic.enemy
        try {
            toggleActivation(enemy != null)
            if (enemy == null) {
                return
            }
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtRound)!!).text = _round.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_btnDamageEnemy)!!).text =
                activity_Navigator.getString(
                    R.string.button_assignDamage,
                    arrayOf<Any?>(enemy.name)
                )
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_btnDamagePlayer)!!).text =
                activity_Navigator.getString(
                    R.string.button_assignDamage,
                    arrayOf<Any?>(playerSheet!!.name)
                )
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyName)!!).text = enemy.name
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyValor)!!).text = enemy.valor.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyCraft)!!).text = enemy.craft.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyME)!!).text = enemy.mentalEnergy.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyPE)!!).text = enemy.phisicalEnergy.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyProtection)!!).text =
                enemy.protection.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtEnemyDamage)!!).text =
                enemy.damage.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtNotes)!!).text = enemy.note
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerName)!!).text =
                playerSheet.name
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerValor)!!).text =
                _playerValor.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerCraft)!!).text =
                _playerCraft.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerDamage)!!).text =
                _playerDamage.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerProtection)!!).text =
                _playerProtection.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerPE)!!).text =
                _playerPE.toString()
            (activity_Navigator.findViewById<TextView>(R.id.frgCombat_txtPlayerME)!!).text =
                _playerME.toString()
            if (!bool || enemy.note.equals("")) {
                return
            }
            DialogManager.showCombatNotes(activity_Navigator)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun highlightBackground(i: Int, i2: Int) {
        val textView: TextView = GameLogic.navigator!!.findViewById(i)
        textView.background.level = 2
        Handler().postDelayed({ textView.background.level = 1 }, i2.toLong())
    }

    fun onClickView(view: View) {
        try {
            val enemy: Enemy = GameLogic.enemy ?: return
            when (view.id) {
                R.id.frgCombat_btnAddEnemyCraft -> enemy.craft += 1
                R.id.frgCombat_btnAddEnemyDamage -> enemy.damage += 1
                R.id.frgCombat_btnAddEnemyME -> enemy.mentalEnergy += 1
                R.id.frgCombat_btnAddEnemyPE -> {
                    val phisicalEnergy: Int = enemy.phisicalEnergy
                    enemy.phisicalEnergy += 1
                    checkTrigger(
                        enemy,
                        Enemy.Condition.ENEMY_EF,
                        phisicalEnergy,
                        enemy.phisicalEnergy
                    )
                }

                R.id.frgCombat_btnAddEnemyProtection -> enemy.protection += 1
                R.id.frgCombat_btnAddEnemyValor -> enemy.valor += 1
                R.id.frgCombat_btnAddPlayerCraft -> _playerCraft =
                    getNormalizedValue(_playerCraft + 1)

                R.id.frgCombat_btnAddPlayerDamage -> _playerDamage =
                    getNormalizedValue(_playerDamage + 1)

                R.id.frgCombat_btnAddPlayerME -> _playerME = getNormalizedValue(_playerME + 1)
                R.id.frgCombat_btnAddPlayerPE -> {
                    val i = _playerPE
                    _playerPE = getNormalizedValue(_playerPE + 1)
                    checkTrigger(enemy, Enemy.Condition.PLAYER_EF, i, _playerPE)
                }

                R.id.frgCombat_btnAddPlayerProtection -> _playerProtection =
                    getNormalizedValue(_playerProtection + 1)

                R.id.frgCombat_btnAddPlayerValor -> _playerValor =
                    getNormalizedValue(_playerValor + 1)

                R.id.frgCombat_btnAddRound -> {
                    val i2 = _round
                    _round = getNormalizedValue(_round + 1)
                    checkTrigger(enemy, Enemy.Condition.TURN, i2, _round)
                }

                R.id.frgCombat_btnDamageEnemy -> {
                    val iMax = (_playerDamage - enemy.protection).coerceAtLeast(0)
                    val phisicalEnergy2: Int = enemy.phisicalEnergy
                    enemy.phisicalEnergy = ((enemy.phisicalEnergy - iMax).coerceAtLeast(0))
                    highlightBackground(R.id.frgCombat_txtEnemyPE, 2000)
                    damageToast(iMax, enemy.name)
                    checkTrigger(
                        enemy,
                        Enemy.Condition.ENEMY_EF,
                        phisicalEnergy2,
                        enemy.phisicalEnergy
                    )
                    if (enemy.phisicalEnergy == 0) {
                        DialogManager.showCombatTriggerWarning(
                            GameLogic.navigator,
                            GameLogic.navigator!!.getString(R.string.dialog_enemyIsDead)
                        )
                    }
                }

                R.id.frgCombat_btnDamagePlayer -> {
                    val iMax2 = (enemy.damage - _playerProtection).coerceAtLeast(0)
                    val i3 = _playerPE
                    _playerPE = max(_playerPE - iMax2, 0)
                    highlightBackground(R.id.frgCombat_txtPlayerPE, 2000)
                    damageToast(iMax2, GameLogic.playerSheet!!.name)
                    checkTrigger(enemy, Enemy.Condition.PLAYER_EF, i3, _playerPE)
                    if (_playerPE == 0) {
                        DialogManager.showCombatTriggerWarning(
                            GameLogic.navigator,
                            GameLogic.navigator!!.getString(R.string.dialog_characterIsDeadByEF)
                        )
                    }
                }

                R.id.frgCombat_btnSubtractEnemyCraft -> enemy.craft -= 1
                R.id.frgCombat_btnSubtractEnemyDamage -> enemy.damage -= 1
                R.id.frgCombat_btnSubtractEnemyME -> enemy.mentalEnergy -= 1
                R.id.frgCombat_btnSubtractEnemyPE -> {
                    val phisicalEnergy3: Int = enemy.phisicalEnergy
                    enemy.phisicalEnergy -= 1
                    checkTrigger(
                        enemy,
                        Enemy.Condition.ENEMY_EF,
                        phisicalEnergy3,
                        enemy.phisicalEnergy
                    )
                }

                R.id.frgCombat_btnSubtractEnemyProtection -> enemy.protection -= 1
                R.id.frgCombat_btnSubtractEnemyValor -> enemy.valor -= 1
                R.id.frgCombat_btnSubtractPlayerCraft -> _playerCraft =
                    getNormalizedValue(_playerCraft - 1)

                R.id.frgCombat_btnSubtractPlayerDamage -> _playerDamage =
                    getNormalizedValue(_playerDamage - 1)

                R.id.frgCombat_btnSubtractPlayerME -> _playerME = getNormalizedValue(_playerME - 1)
                R.id.frgCombat_btnSubtractPlayerPE -> {
                    val i4 = _playerPE
                    _playerPE = getNormalizedValue(_playerPE - 1)
                    checkTrigger(enemy, Enemy.Condition.PLAYER_EF, i4, _playerPE)
                }

                R.id.frgCombat_btnSubtractPlayerProtection -> _playerProtection =
                    getNormalizedValue(_playerProtection - 1)

                R.id.frgCombat_btnSubtractPlayerValor -> _playerValor =
                    getNormalizedValue(_playerValor - 1)

                R.id.frgCombat_btnSubtractRound -> {
                    val i5 = _round
                    _round = getNormalizedValue(_round - 1)
                    checkTrigger(enemy, Enemy.Condition.TURN, i5, _round)
                }

                R.id.frgCombat_btnTerminate -> terminateCombat()
                R.id.frgCombat_btnTutorial -> {
                    val intent =
                        Intent(GameLogic.navigator, TutorialActivity::class.java as Class<*>)
                    intent.putExtra(GameConstants.TUTORIAL_HOME, GameConstants.TUTORIAL_HOME_COMBAT)
                    GameLogic.navigator!!.startActivity(intent)
                    return
                }
            }
            initCombat(false)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun checkTrigger(enemy: Enemy, condition: Enemy.Condition, i: Int, i2: Int) {
        val triggers: ArrayList<Enemy.Trigger?> = enemy.triggers
        for (i3 in triggers.indices) {
            val trigger = triggers[i3] ?: continue
            if (trigger.condition!!.value == condition.value && trigger.activated != true
                    && ((trigger.overThreshold == true && i < trigger.threshold && i2 >= trigger.threshold)
                    || (trigger.overThreshold != true && i > trigger.threshold && i2 <= trigger.threshold))
            ) {
                trigger.activated = true
                DialogManager.showCombatTriggerWarning(GameLogic.navigator, trigger.note)
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    fun damageToast(i: Int, str: String?) {
        val string = if (i == 1) {
            GameLogic.navigator!!.getString(
                R.string.toast_assignDamageSingle,
                arrayOf<Any?>(str, i)
            )
        } else if (i > 1) {
            GameLogic.navigator!!.getString(
                R.string.toast_assignDamage,
                arrayOf<Any?>(str, i)
            )
        } else {
            GameLogic.navigator!!.getString(R.string.toast_absorbDamage, arrayOf<Any?>(str))
        }
        ToastManager.showGenericToast(GameLogic.navigator, string)
    }
}