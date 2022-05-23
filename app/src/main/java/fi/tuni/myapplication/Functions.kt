package fi.tuni.myapplication

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Functions {
    fun getUrl(url: String) : String? {
        var result: String? = null
        val sb = StringBuffer()
        val myUrl = URL(url)
        val conn = myUrl.openConnection() as HttpURLConnection
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()))

        reader.use {
            var line: String? = null

            do {
                line = it.readLine()
                sb.append(line)
            } while(line !== null)
            result = sb.toString()
        }
        return result
    }
}