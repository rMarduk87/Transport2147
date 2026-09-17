package rpt.games.transport2147.utils.view.collection

import android.content.Context
import rpt.games.transport2147.R
import rpt.games.transport2147.utils.data.enums.AbilityTypeEnum
import rpt.games.transport2147.utils.data.enums.BaseValueModeEnum
import rpt.games.transport2147.utils.data.enums.ObjectTypeEnum
import rpt.games.transport2147.utils.managers.DialogManager
import rpt.games.transport2147.utils.managers.ToastManager
import rpt.games.transport2147.utils.view.game.PlayerSheet


class ConditionalArrayList<E>(
    private val _context: Context,
    private val _father: PlayerSheet,
    private val _objectType: ObjectTypeEnum,
    private var _checkConditions: Boolean
) : ArrayList<E?>() {
    fun setConditionCheckFlag(z: Boolean) {
        this._checkConditions = z
    }


    override fun add(e: E?): Boolean {
        val zAdd = super.add(e)
        if (this._checkConditions) {
            when (this._objectType) {
                ObjectTypeEnum.ARMOR -> ToastManager.showGenericToast(
                    this._context,
                    this._context.getString(if (super.size > 1) 
                        R.string.toast_condTooManyArmors else R.string.toast_condCheckProtectionValue)
                )

                ObjectTypeEnum.WEAPON -> ToastManager.showGenericToast(
                    this._context,
                    this._context.getString(if (super.size > 2) 
                        R.string.toast_condTooManyWeapons else R.string.toast_condCheckDamageValue)
                )

                ObjectTypeEnum.ITEM -> if (super.size >
                    this._father.getAbility(AbilityTypeEnum.VALOR)!!
                        .getBaseValue(BaseValueModeEnum.ACTUAL)!!.value * 2
                ) {
                    DialogManager.showWarningOnChangeSheetValue(
                        R.string.dialog_condBackpackOverloaded
                    )
                }

                else -> {}
            }
        }
        return zAdd
    }
    
    override fun remove(obj: Any?): Boolean {
        super.remove(obj)
        if (!this._checkConditions) {
            return true
        }
        when (this._objectType) {
            ObjectTypeEnum.ARMOR -> ToastManager.showGenericToast(
                this._context,
                this._context.getString(R.string.toast_condCheckProtectionValue)
            )

            ObjectTypeEnum.WEAPON -> ToastManager.showGenericToast(
                this._context,
                this._context.getString(R.string.toast_condCheckDamageValue)
            )

            else -> {}
        }
        return true
    }
}