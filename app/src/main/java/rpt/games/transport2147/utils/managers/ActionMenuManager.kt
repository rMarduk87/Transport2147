package rpt.games.transport2147.utils.managers

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import rpt.com.base.log.e
import rpt.games.transport2147.GlossaryActivity
import rpt.games.transport2147.HistoryActivity
import rpt.games.transport2147.MainMenuActivity
import rpt.games.transport2147.NavigatorActivity
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.GameConstants
import rpt.games.transport2147.utils.data.appmodels.complex.History
import rpt.games.transport2147.utils.view.game.DiceRollerDispatcher
import rpt.games.transport2147.utils.view.game.GameLogic

object ActionMenuManager {
    fun onActionMenuClicked(context: Context, menuItem: MenuItem): Boolean {
        var z = true
        try {
            val itemId = menuItem.itemId
            if (itemId != R.id.mnuMainMenu_itmGlossary) {
                try {
                    when (itemId) {
                        R.id.mnuNavigator_itmChangeRoller1, R.id.mnuNavigator_itmChangeRoller2 -> actionChangeRoller(
                            context,
                            menuItem
                        )

                        R.id.mnuNavigator_itmDiceRoller1, R.id.mnuNavigator_itmDiceRoller2 -> actionRollDice(
                            context,
                            menuItem
                        )

                        R.id.mnuNavigator_itmEnableDisableHistory -> actionEnableDisableHistory(
                            menuItem
                        )

                        R.id.mnuNavigator_itmGoChapter -> actionGoToChapter(context)
                        R.id.mnuNavigator_itmHistory -> actionGoToHistory(context)
                        R.id.mnuNavigator_itmHome -> {
                            GameLogic.navigator!!.startActivity(
                                Intent(
                                    GameLogic.navigator,
                                    MainMenuActivity::class.java as Class<*>
                                )
                            )
                            GameLogic.navigator!!.finish()
                        }

                        R.id.mnuNavigator_itmPreviousChapter -> actionGoToPreviousChapter(context)
                    }
                    return true
                } catch (e: Exception) {
                    e.message?.let { e(Throwable(e),it) }
                    return z
                }
            }
            val intent = Intent(GameLogic.navigator,
                GlossaryActivity::class.java as Class<*>)
            intent.putExtra(GameConstants.GLOSSARY_HOME,
                GameConstants.GLOSSARY_HOME_NAVIGATOR)
            GameLogic.navigator!!.startActivity(intent)
            return false
        } catch (e2: Exception) {
            e2.message?.let { e(Throwable(e2),it) }
            z = false
        }
        return false
    }

    fun actionEnableDisableHistory(menuItem: MenuItem) {
        History.historyEnabled = !History.historyEnabled
        updateHistoryMenu(menuItem)
        updateHistoryIcon()
        ToastManager.showGenericToast(
            GameLogic.navigator,
            GameLogic.navigator!!.getString(if (History.historyEnabled)
                R.string.toast_historyEnabled else R.string.toast_historyDisabled)
        )
    }

    fun updateHistoryMenu(menuItem: MenuItem) {
        menuItem.setTitle(if (History.historyEnabled) R.string.menu_enabledHistory else R.string.menu_disabledHistory)
    }

    fun updateHistoryIcon() {
        GameLogic.iconToggleHistory!!.drawable.level = if (History.historyEnabled) 1 else 2
    }

    fun actionChangeRoller(context: Context, menuItem: MenuItem) {
        val dices: Int
        try {
            val builder = AlertDialog.Builder(context)
            val viewInflate: View =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_changedice,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            DialogManager.setCustomDialogTitle(
                context,
                viewInflate,
                R.string.dialog_changeDiceRollerTitle
            )
            dices = if (menuItem.itemId == R.id.mnuNavigator_itmChangeRoller1) {
                GameLogic.diceRoller1!!.dices
            } else {
                GameLogic.diceRoller2!!.dices
            }
            viewInflate.findViewById<RadioButton>(
                DiceRollerDispatcher.getFromNum(dices)!!.id
            ).isChecked = true
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            viewInflate.findViewById<RadioGroup>(R.id.dlgChangeDices_rdgDices).setOnCheckedChangeListener { _, checkedId ->
                val diceRollerManager = if (menuItem.itemId == R.id.mnuNavigator_itmChangeRoller1)
                    GameLogic.diceRoller1 else GameLogic.diceRoller2
                diceRollerManager?.dices = DiceRollerDispatcher.getFromId(checkedId)!!.num
                diceRollerManager?.setUsage(false)
                alertDialogCreate.dismiss()
                GameLogic.navigator!!.invalidateOptionsMenu()
            }
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun actionGoToChapter(context: Context) {
        try {
            val builder = AlertDialog.Builder(context)
            val viewInflate: View =
                (context.getSystemService("layout_inflater") as LayoutInflater).inflate(
                    R.layout.dialog_gotochapter,
                    null as ViewGroup?
                )
            builder.setView(viewInflate)
            DialogManager.setCustomDialogTitle(context, viewInflate, R.string.dialog_goToChapterTitle)
            (viewInflate.findViewById<View?>(R.id.dlgGoToChapter_message) as TextView).text =
                context.getText(R.string.dialog_goToChapterMessage)
            val editText =
                viewInflate.findViewById<View?>(R.id.dlgChangeChapter_txtChapter) as EditText
            val alertDialogCreate = builder.create()
            alertDialogCreate.show()
            DialogManager.setCustomOkButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonOk
            ) {
                val string = editText.text.toString()
                try {
                    BookManager.getChapter(string)
                    actionLoadChapter(context, string)
                    alertDialogCreate.dismiss()
                } catch (unused: Exception) {
                    ToastManager.error_invalidChapter(context, string)
                }
            }
            DialogManager.setCustomCancelButton(
                context,
                alertDialogCreate,
                R.string.dialog_buttonCancel,
                null as View.OnClickListener?
            )
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun actionLoadChapter(context: Context, str: String?) {
        try {
            History.requestedChapter = str
            context.startActivity(Intent(context, NavigatorActivity::class.java as Class<*>))
            GameLogic.navigator!!.changePagerPage(0)
            GameLogic.diceRoller1!!.setUsage(false)
            GameLogic.diceRoller2!!.setUsage(false)
            GameLogic.chapterFragment!!.loadChapter()
            ToastManager.goToChapter(context, str!!)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun actionGoToHistory(context: Context) {
        try {
            context.startActivity(Intent(context, HistoryActivity::class.java as Class<*>))
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun actionGoToPreviousChapter(context: Context) {
        try {
            if (History.size < 2) {
                return
            }
            History.removeLastChapter()
            actionLoadChapter(context, History.lastChapter!!.chapter)
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }

    fun actionRollDice(context: Context, menuItem: MenuItem) {
        try {
            (if (menuItem.itemId == R.id.mnuNavigator_itmDiceRoller1)
                GameLogic.diceRoller1 else GameLogic.diceRoller2)!!.toggleRollDices()
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
        }
    }
}