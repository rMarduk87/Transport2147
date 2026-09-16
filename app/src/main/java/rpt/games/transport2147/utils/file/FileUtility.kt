package rpt.games.transport2147.utils.file

import rpt.games.transport2147.TransportApplication

class FileUtility {
    companion object {
        fun loadResourceFileAsString(instance: TransportApplication, transport2147: Int): String? {
            val inputStream = instance.resources.openRawResource(transport2147)
            val reader = inputStream.bufferedReader()
            val stringBuilder = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                stringBuilder.append("\n")
                line = reader.readLine()
            }
            return stringBuilder.toString()
        }
    }
}