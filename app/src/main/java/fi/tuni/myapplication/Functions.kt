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

    fun getTime(milliseconds: Int) : String {
        val hours = milliseconds / 1000 / 60 / 60 % 24
        var minutes = (milliseconds / 1000 / 60 % 60).toString()
        if(minutes.length == 1) {
            minutes = "0$minutes"
        }
        var seconds = (milliseconds / 1000 % 60).toString()
        if(seconds.length == 1) {
            seconds = "0$seconds"
        }
        val tenths = milliseconds / 100 % 10
        if(hours == 0 && minutes == "00") {
            return "$seconds.$tenths"
        }
        if(hours == 0) {
            return "$minutes:$seconds.$tenths"
        }
        return "$hours:$minutes:$seconds.$tenths"
    }
}