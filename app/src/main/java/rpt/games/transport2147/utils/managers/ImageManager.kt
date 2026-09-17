package rpt.games.transport2147.utils.managers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import androidx.core.content.FileProvider
import rpt.games.transport2147.utils.AppUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object ImageManager {
    @Throws(IOException::class)
    fun getDrawableFromAsset(context: Context, str: String): Drawable? {
        val inputStreamOpen = context.assets.open(str)
        val drawableCreateFromStream = Drawable.createFromStream(inputStreamOpen, null)
        inputStreamOpen.close()
        return drawableCreateFromStream
    }

    fun getUriFromAsset(context: Context, str: String): Uri? {
        val file: File =
            File(Environment.getExternalStorageDirectory(), "megeraShare.jpg")
        try {
            file.createNewFile()
            val inputStreamOpen = context.assets.open(str)
            val fileOutputStream: FileOutputStream = FileOutputStream(file)
            val bArr = ByteArray(10240)
            while (true) {
                val i = inputStreamOpen.read(bArr)
                if (i == -1) {
                    break
                }
                fileOutputStream.write(bArr, 0, i)
            }
            inputStreamOpen.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return FileProvider.getUriForFile(context, "rpt.games.transport2147.provider", file)
    }

    @Throws(IOException::class)
    fun getScaledBitmapFromAsset(context: Context, str: String, i: Int, i2: Int): Bitmap? {
        var i = i
        var i2 = i2
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val inputStreamOpen = context.assets.open(str)
        BitmapFactory.decodeStream(inputStreamOpen, null, options)
        inputStreamOpen.close()
        if (i == 0 || i2 == 0) {
            val displayMetrics: DisplayMetrics = AppUtils.getDisplayMetrics(context)
            val i3 = displayMetrics.widthPixels
            i2 = displayMetrics.heightPixels
            i = i3
        }
        options.inSampleSize = calculateInSampleSize(options, i, i2)
        options.inJustDecodeBounds = false
        val inputStreamOpen2 = context.assets.open(str)
        val bitmapDecodeStream = BitmapFactory
            .decodeStream(inputStreamOpen2, null, options)
        inputStreamOpen2.close()
        return bitmapDecodeStream
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, i: Int, i2: Int): Int {
        val i3 = options.outHeight
        val i4 = options.outWidth
        var i5 = 1
        if (i3 > i2 || i4 > i) {
            val i6 = i3 / 2
            val i7 = i4 / 2
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2
            }
        }
        return i5
    }
}