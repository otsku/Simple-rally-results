package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.concurrent.thread

/**
 * Activity to show a list of rally results
 *
 * @author Otto Kujala
 *
 */
class ResultsActivity() : AppCompatActivity() {
    private lateinit var stage: Stage
    private lateinit var listview: ListView
    private lateinit var adapter: MyStageAdapter
    private lateinit var adapterTime: MyStageTimeAdapter
    private lateinit var entrants: Entrants
    private var eventId: Int = 0
    private var functions: Functions = Functions()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        stage = intent.getSerializableExtra("stage") as Stage
        entrants = intent.getSerializableExtra("entrants") as Entrants
        eventId = intent.getSerializableExtra("event_id") as Int
        listview = findViewById<ListView>(R.id.listView)
        adapter = MyStageAdapter(this, R.layout.stage_item, ArrayList<Result>(), ArrayList<Entrant?>())
        adapterTime = MyStageTimeAdapter(this, R.layout.stage_item, ArrayList<StageTime>(), ArrayList<Entrant?>())
        listview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        thread() {
            if(adapter.getList().size == 0) {
                val mp = ObjectMapper()
                var resultsraw = functions.getUrl("https://api.wrc.com/results-api/rally-event/" + eventId + "/stage-result/stage-external/" + stage.stageId)
                resultsraw = resultsraw?.dropLast(4)
                resultsraw = "{\"results\":" + resultsraw.toString() + "}"
                val results: Results = mp.readValue(resultsraw, Results::class.java)
                results.results?.forEach {
                    runOnUiThread() {
                        var entrant: Entrant? = null
                        for(i in entrants.entrants!!) {
                            if(it.entryId == i.entryId) {
                                entrant = i
                            }
                        }
                        adapter.add(it, entrant)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}