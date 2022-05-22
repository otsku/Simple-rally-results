package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.content.Context;
import android.util.Log
import java.util.ArrayList;

class MyEventAdapter(context: Context, resource: Int, private val arrayList:ArrayList<Stage> , private val winners:ArrayList<Entrant?>) : ArrayAdapter<Stage>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var winner: TextView
    private lateinit var time: TextView

    fun add(item: Stage, winner: Entrant?) {
        arrayList.add(item)
        winners.add(winner)
    }

    fun getList(): ArrayList<Stage> {
        return arrayList
    }

    override fun getItem(position: Int): Stage {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertview = convertView
        convertview = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)
        eventcount = convertview.findViewById(R.id.countTextView)
        name = convertview.findViewById(R.id.nameTextView)
        winner = convertview.findViewById(R.id.winnerTextView)
        time = convertview.findViewById(R.id.timeTextView)
        eventcount.text = (position + 1).toString()
        name.text = arrayList[position].stageName
        winner.text = "Stage winner: " + winners[position]?.driver?.lastName.toString()
        val timeString = getTime(arrayList[position].elapsedDurationMs!!)
        time.text = "Best time: " + timeString
        return convertview
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
