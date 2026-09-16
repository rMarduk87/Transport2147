package rpt.games.transport2147.utils.managers

import android.content.Context
import android.widget.Toast
import rpt.games.transport2147.R


class ToastManager {
    companion object {
        fun goToChapter(context: Context, str: String) {
            execToast(context, String.format(context.getString(R.string.toast_goToChapter),
                str))
        }

        fun showError(context: Context, str: String) {
            execToast(context, String.format(context.getString(R.string.toast_errorGeneric),
                str))
        }

        fun showGenericToast(context: Context?, str: String?) {
            execToast(context, str)
        }

        fun error_invalidChapter(context: Context, str: String) {
            execToast(
                context,
                String.format(context.getString(R.string.toast_errorChapterNotFound),
                    str)
            )
        }

        private fun execToast(context: Context?, str: String?) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }
}