package rpt.games.transport2147.utils.view.string

import android.text.SpannableString
import androidx.core.view.GravityCompat


class RPSpannableString(charSequence: CharSequence?) : SpannableString(charSequence) {
    var gravity: Int = GravityCompat.START
}