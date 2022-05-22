package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.content.Context;
import java.util.ArrayList;

class MyEventAdapter(context: Context, resource: Int, private val arrayList:ArrayList<Stage> , private val winners:ArrayList<Entrant?>) : ArrayAdapter<Stage>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var winner: TextView

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
        convertview = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        eventcount = convertview.findViewById(R.id.countTextView)
        name = convertview.findViewById(R.id.nameTextView)
        winner = convertview.findViewById(R.id.winnerTextView)
        eventcount.text = (position + 1).toString()
        name.text = arrayList[position].stageName
        winner.text = winners[position]?.driver?.lastName.toString()
        return convertview
    }
}
