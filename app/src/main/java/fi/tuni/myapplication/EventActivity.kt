package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
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
data class Country(var countryId: Int? = 0, var name: String? = null, var iso2: String? = null, var iso3: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Driver(var personId: Int? = 0, var country: Country? = null, var firstName: String? = null, var lastName: String? = null, var abbvName: String? = null, var fullName: String? = null, var code: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Manufacturer(var manufacturerId : Int? = 0, var name: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Group(var groupId : Int? = 0, var name: String? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Entrant(var entryId: Int? = 0, var driver : Driver? = null, var manufacturer: Manufacturer? = null, var status: String? = null, var group: Group? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Entrants(var entrants: MutableList<Entrant>? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Stage(var stageId: Int? = 0, var entryId: Int? = 0, var stageName: String? = null, var elapsedDurationMs: Int? = 0) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Stages(var stages: MutableList<Stage>? = null) : Serializable

class EventActivity() : AppCompatActivity() {
    private lateinit var item: Items
    private lateinit var listview: ListView
    private lateinit var adapter: MyEventAdapter
    private lateinit var entrants: Entrants
    private lateinit var winner: TextView
    private lateinit var nation: TextView
    private lateinit var days: TextView
    private lateinit var event: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        winner = findViewById(R.id.winnerTextView)
        nation = findViewById(R.id.nationTextView)
        days = findViewById(R.id.daysTextView)
        event = findViewById(R.id.eventTextView)
        item = intent.getSerializableExtra("item") as Items
        val lastDay: Int = item.eventDays?.size as Int - 1
        days.text = item.eventDays?.get(0)?.eventDay + " - " + item.eventDays?.get(lastDay)?.eventDay
        event.text = item.name
        if(item.winner?.lastName != null) winner.text = "Event winner: " + item.winner?.lastName
        else winner.text = ""
        if(item.winner?.nation?.name != null) nation.text = "Winners country: " + item.winner?.nation?.name
        else nation.text = ""
        listview = findViewById<ListView>(R.id.listView)
        adapter = MyEventAdapter(this, R.layout.item, ArrayList<Stage>(), ArrayList<Entrant?>())
        listview.adapter = adapter
        listview.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem: Serializable = parent.getItemAtPosition(position) as Serializable
            val intent = Intent(
                this@EventActivity,
                StageActivity::class.java
            )
            intent.putExtra("stage", selectedItem)
            intent.putExtra("event_id", item.id)
            intent.putExtra("entrants", entrants)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        thread() {
            val mp = ObjectMapper()
            var cars = getUrl("https://api.wrc.com/results-api/rally-event/" + item.id + "/cars")
            cars = cars?.dropLast(4)
            cars = "{\"entrants\":" + cars.toString() + "}"
            entrants = mp.readValue(cars, Entrants::class.java)
            var stagesraw = getUrl("https://api.wrc.com/results-api/rally-event/" + item.id + "/stage-winners")
            stagesraw = stagesraw?.dropLast(4)
            stagesraw = "{\"stages\":" + stagesraw.toString() + "}"
            val stages: Stages = mp.readValue(stagesraw, Stages::class.java)
            if(adapter.getList().size == 0) {
                stages.stages?.forEach {
                    runOnUiThread() {
                        var winner: Entrant? = null
                        for(i in entrants.entrants!!) {
                            if(it.entryId == i.entryId) {
                                winner = i
                            }
                        }
                        adapter.add(it, winner)
                        adapter.notifyDataSetChanged()
                    }
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