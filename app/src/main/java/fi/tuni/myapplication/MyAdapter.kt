package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyAdapter(context: Context, resource: Int, private val arrayList: ArrayList<Items>) : ArrayAdapter<Items>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var winner: TextView

    fun add(item: Items) {
        arrayList.add(item)
    }

    fun getList(): ArrayList<Items> {
        return arrayList
    }

    override fun getItem(position: Int): Items {
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
        name.text = arrayList[position].name
        winner.text = arrayList[position].winner?.lastName
        return convertview
    }
}