import java.util.regex.Pattern

fun main() {
    val str = "chapter"
    val str2 = "name"
    val str3 = """
        <chapters>
            <chapter name ="INTRODUZIONE">
                <title>INTRODUZIONE</title>
                <p>Hello
                World</p>
            </chapter>
            <chapter name ="REGOLE">
                <p>Rules</p>
            </chapter>
        </chapters>
    """.trimIndent()

    val matcher = Pattern.compile("<$str\\s+.*?$str2\\s*=\"(.*?)\"\\s*.*?>.*?</$str>", Pattern.DOTALL)
        .matcher(str3)
    
    var count = 0
    while (matcher.find()) {
        println("Found: " + matcher.group(1))
        count++
    }
    println("Total: " + count)
}
