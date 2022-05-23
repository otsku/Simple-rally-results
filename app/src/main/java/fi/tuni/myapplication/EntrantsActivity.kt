package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.concurrent.thread

class EntrantsActivity() : AppCompatActivity() {
    private lateinit var entrants: Entrants
    private lateinit var listview: ListView
    private lateinit var adapter: MyEntrantAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrants)
        entrants = intent.getSerializableExtra("entrants") as Entrants
        listview = findViewById<ListView>(R.id.listView)
        adapter = MyEntrantAdapter(this, R.layout.entrant_item, ArrayList<Entrant>())
        listview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        thread() {
            if(adapter.getList().size == 0) {
                entrants.entrants?.forEach {
                    runOnUiThread() {
                        adapter.add(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}