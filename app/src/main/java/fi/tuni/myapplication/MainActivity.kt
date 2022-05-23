package fi.tuni.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Nation class with all nation data
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class Nation(var id: Int? = 0, var name: String? = null, var alpha2: String? = null, var alpha3: String? = null, var ioc: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Winner class with all winner data
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class Winner(var id: Int? = 0, var firstName: String? = null, var lastName: String? = null, var nation: Nation? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Status class with all status data
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class Status(var id: Int? = 0, var name: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * EventDay class with all eventDay data
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class EventDay(var id: Int? = 0, var eventDay: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Items class with all item data
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class Items(var id: Int? = 0, var name: String? = null, var nation: Nation? = null, var winner: Winner? = null, var status: Status? = null, var eventDays: MutableList<EventDay>? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * List of RallyEvents
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class RallyEvents(var total : Int? = 0, var items: MutableList<Items>? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * RallyEvents class with all rallyEvents data
 * made for jackson object mapping
 *
 * @author Otto Kujala
 *
 */
data class WrcJsonObject(var rallyEvents: RallyEvents? = null) : Serializable

/**
 * Activity to show a list of rally events
 *
 * @author Otto Kujala
 *
 */
class MainActivity : AppCompatActivity() {
    private lateinit var listview: ListView
    private lateinit var adapter: MyAdapter
    private var functions: Functions = Functions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listview = findViewById<ListView>(R.id.listView)
        adapter = MyAdapter(this, R.layout.item, ArrayList<Items>())
        listview.adapter = adapter
        listview.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem: Serializable = parent.getItemAtPosition(position) as Serializable
            val intent = Intent(
                this@MainActivity,
                EventActivity::class.java
            )
            intent.putExtra("item", selectedItem);
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        thread() {
            if(adapter.getList().size == 0) {
                val stuff = functions.getUrl("https://api.wrc.com/contel-page/83388/calendar/active-season/")
                val mp = ObjectMapper()
                val myObject: WrcJsonObject = mp.readValue(stuff, WrcJsonObject::class.java)
                val events: RallyEvents? = myObject.rallyEvents
                val items: MutableList<Items>? = events?.items
                items?.forEach {
                    runOnUiThread() {
                        adapter.add(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}