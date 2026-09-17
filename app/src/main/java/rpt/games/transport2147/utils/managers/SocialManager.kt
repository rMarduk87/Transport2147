package rpt.games.transport2147.utils.managers


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import rpt.games.transport2147.R
import androidx.core.net.toUri

object SocialManager {
    
    fun sendToWhatsApp(context: Context, str: String?) {
        val packageManager = context.packageManager
        try {
            val intent = Intent("android.intent.action.SEND")
            intent.type = "text/plain"
            packageManager.getPackageInfo("com.whatsapp", 128)
            intent.setPackage("com.whatsapp")
            intent.putExtra("android.intent.extra.TEXT", str)
            context.startActivity(Intent.createChooser(intent, "Share with"))
        } catch (unused: PackageManager.NameNotFoundException) {
            DialogManager.showGenericDialog(
                context,
                context.getString(R.string.rem_info),
                context.getString(R.string.dialog_whatAppNotInstalled)
            )
        }
    }

    fun sendToWhatsApp(context: Context, str: String?, str2: String?) {
        val packageManager = context.packageManager
        try {
            val intent = Intent("android.intent.action.SEND")
            intent.type = "image/jpg"
            packageManager.getPackageInfo("com.whatsapp", 128)
            intent.setPackage("com.whatsapp")
            intent.putExtra("android.intent.extra.TEXT", str)
            intent.putExtra("android.intent.extra.STREAM",
                ImageManager.getUriFromAsset(context, str2!!))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
        } catch (unused: PackageManager.NameNotFoundException) {
            DialogManager.showGenericDialog(
                context,
                context.getString(R.string.rem_info),
                context.getString(R.string.dialog_whatAppNotInstalled)
            )
        }
    }

    fun sendGeneric(context: Context, str: String?) {
        val intent = Intent()
        intent.action = "android.intent.action.SEND"
        intent.putExtra("android.intent.extra.TEXT", str)
        intent.type = "text/plain"
        context.startActivity(intent)
    }

    fun openUrl(context: Context, str: String?) {
        val intent = Intent("android.intent.action.VIEW")
        intent.data = Uri.parse(str)
        context.startActivity(intent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun openGooglePlay(context: Context, str: String?) {
        val intent = Intent("android.intent.action.VIEW",
            ("market://details?id=$str").toUri())
        var z = false
        for (resolveInfo in context.packageManager.queryIntentActivities(intent, 0)) {
            if (resolveInfo.activityInfo.applicationInfo.packageName == "com.android.vending") {
                val activityInfo = resolveInfo.activityInfo
                val componentName =
                    ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.component = componentName
                context.startActivity(intent)
                z = true
                break
            }
        }
        if (z) {
            return
        }
        context.startActivity(
            Intent(
                "android.intent.action.VIEW",
                ("https://play.google.com/store/apps/details?id=$str").toUri()
            )
        )
    }
}