package fi.tuni.myapplication

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
data class StageTime(var entryId: Int? = 0, var elapsedDurationMs: Int? = 0, var status: String? = null, var position: Int? = 0, var diffFirstMs: Int? = 0) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class StageTimes(var times: MutableList<StageTime>? = null) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(var entryId: Int? = 0, var stageTimeMs: Int? = 0, var totalTimeMs: Int? = 0, var position: Int? = 0, var diffFirstMs: Int? = 0) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class Results(var results: MutableList<Result>? = null) : Serializable

class StageActivity() : AppCompatActivity() {
    private lateinit var stage: Stage
    private lateinit var listview: ListView
    private lateinit var adapter: MyStageAdapter
    private lateinit var entrants: Entrants
    private var eventId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stage)
        stage = intent.getSerializableExtra("stage") as Stage
        entrants = intent.getSerializableExtra("entrants") as Entrants
        eventId = intent.getSerializableExtra("event_id") as Int
        listview = findViewById<ListView>(R.id.listView)
        adapter = MyStageAdapter(this, R.layout.item, ArrayList<Result>(), ArrayList<Entrant?>(), ArrayList<StageTime?>())
        listview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        thread() {
            val mp = ObjectMapper()
            var resultsraw = getUrl("https://api.wrc.com/results-api/rally-event/" + eventId + "/stage-result/stage-external/" + stage.stageId)
            resultsraw = resultsraw?.dropLast(4)
            resultsraw = "{\"results\":" + resultsraw.toString() + "}"
            val results: Results = mp.readValue(resultsraw, Results::class.java)

            var timesraw = getUrl("https://api.wrc.com/results-api/rally-event/" + eventId + "/stage-times/stage-external/" + stage.stageId)
            timesraw = timesraw?.dropLast(4)
            timesraw = "{\"times\":" + timesraw.toString() + "}"
            Log.d("Message", timesraw.toString())
            val times: StageTimes = mp.readValue(timesraw, StageTimes::class.java)

            if(adapter.getList().size == 0) {
                results.results?.forEach {
                    runOnUiThread() {
                        var entrant: Entrant? = null
                        for(i in entrants.entrants!!) {
                            if(it.entryId == i.entryId) {
                                entrant = i
                            }
                        }
                        var time: StageTime? = null
                        for(i in times.times!!) {
                            if(it.entryId == i.entryId) {
                                time = i
                            }
                        }
                        adapter.add(it, entrant, time)
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