package fi.tuni.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


@JsonIgnoreProperties(ignoreUnknown = true)
data class Nation(var id: Int? = 0, var name: String? = null, var alpha2: String? = null, var alpha3: String? = null, var ioc: String? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Winner(var id: Int? = 0, var firstName: String? = null, var lastName: String? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Items(var id: Int? = 0, var name: String? = null, var nation: Nation? = null, var winner: Winner? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RallyEvents(var total : Int? = 0, var items: MutableList<Items>? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WrcJsonObject(var rallyEvents: RallyEvents? = null)

class MainActivity : AppCompatActivity() {
    private lateinit var listview: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listview = findViewById<ListView>(R.id.listView)
        adapter = ArrayAdapter<String>(this, R.layout.item, R.id.myTextView, ArrayList<String>())
        listview.adapter = adapter

        listview.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position)
            println(selectedItem)
        }

        getData()
    }



    fun getData() {
        thread() {
            val stuff = getUrl("https://api.wrc.com/contel-page/83388/calendar/active-season/")

            val mp = ObjectMapper()
            val myObject: WrcJsonObject = mp.readValue(stuff, WrcJsonObject::class.java)
            val events: RallyEvents? = myObject.rallyEvents
            val items: MutableList<Items>? = events?.items
            items?.forEach {
                runOnUiThread() {
                    adapter.add(it.name)
                    println(it)
                }
            }
        }
    }

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