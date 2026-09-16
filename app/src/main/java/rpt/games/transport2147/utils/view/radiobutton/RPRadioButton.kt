package rpt.games.transport2147.utils.view.radiobutton

import android.content.Context
import android.util.AttributeSet
import rpt.games.transport2147.utils.managers.FontManager

class RPRadioButton : androidx.appcompat.widget.AppCompatRadioButton {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet) {
        if (isInEditMode) {
            return
        }
        FontManager.setCustomFont(this, context, attributeSet)
    }

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    ) {
        if (isInEditMode) {
            return
        }
        FontManager.setCustomFont(this, context, attributeSet)
    }
}
