package rpt.games.transport2147.utils.xml

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import rpt.com.base.log.e
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


object XmlUtility {
    fun getDomTree(str: String): Document? {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(InputSource(StringReader(str)))
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            return null
        }
    }

    fun getRootElement(str: String?): Element? {
        if (str == null) return null
        return getDomTree(str)?.documentElement
    }

    fun getElementAttribute(element: Element?, str: String?): String? {
        if (element != null && str != null && element.hasAttribute(str)) {
            return element.getAttribute(str)
        }
        return null
    }

    fun getFirstSubNode(element: Element, str: String?): Element? {
        return try {
            element.getElementsByTagName(str).item(0) as Element?
        } catch (e: Exception) {
            e.message?.let { e(Throwable(e),it) }
            null
        }
    }

    fun getFirstSubNode(element: Element, str: String?, str2: String?, str3: String?): Element? {
        val elementsByTagName = element.getElementsByTagName(str)
        for (i in 0..<elementsByTagName.length) {
            val element2 = elementsByTagName.item(i) as Element
            val elementAttribute = getElementAttribute(element2, str2)
            if (elementAttribute != null && elementAttribute == str3) {
                return element2
            }
        }
        return null
    }

    fun getAllSubNodes(element: Element, str: String?): NodeList {
        return element.getElementsByTagName(str)
    }

    fun formatNode(str: String?, str2: String?, map: MutableMap<String?, String?>?): String {
        val stringBuffer = StringBuffer()
        stringBuffer.append("<$str")
        if (map != null) {
            for (str3 in map.keys) {
                stringBuffer.append(
                    " $str3=\"" + map[str3]!!.replace("\"", "&quot;")
                        .replace("&", "&amp;").
                        replace("<", "&lt;").
                        replace(">", "&gt;") + "\""
                )
            }
        }
        if (str2 == null || str2 == "") {
            stringBuffer.append("/>")
        } else {
            stringBuffer.append(">$str2</$str>")
        }
        return stringBuffer.toString()
    }
}