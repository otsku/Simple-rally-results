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

/**
 * Custom adapter to show events stages
 *
 * @author Otto Kujala
 *
 */
class MyEventAdapter(context: Context, resource: Int, private val arrayList:ArrayList<Stage> , private val winners:ArrayList<Entrant?>) : ArrayAdapter<Stage>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var winner: TextView
    private lateinit var time: TextView
    private var functions: Functions = Functions()

    /**
     * Add Stage and Entrant to list
     * @param item Stage object
     * @param winner Entrant object
     */
    fun add(item: Stage, winner: Entrant?) {
        arrayList.add(item)
        winners.add(winner)
    }

    /**
     * get List of Stages
     * @return List of Stages
     */
    fun getList(): ArrayList<Stage> {
        return arrayList
    }

    /**
     * get Stage
     * @param position position of Stage in a list
     * @return Stage object
     */
    override fun getItem(position: Int): Stage {
        return arrayList[position]
    }

    /**
     * get Stage id in list
     * @param position position of Stage in a list
     * @return Stage id in list
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    /**
     * get Stage id in list
     * @param position position of Stage in a list
     * @param convertView view that has been passed
     * @param parent parent view group
     * @return ListView item with data inserted
     */
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
        val timeString = arrayList[position].elapsedDurationMs?.let { functions.getTime(it) }
        time.text = "Best time: $timeString"
        return convertview
    }
}
