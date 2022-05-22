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
    private lateinit var status: TextView
    private lateinit var country: TextView

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
        status = convertview.findViewById(R.id.statusTextView)
        country = convertview.findViewById(R.id.countryTextView)
        eventcount.text = (position + 1).toString()
        name.text = arrayList[position].name
        country.text = "Country: " + arrayList[position].nation?.name
        status.text = "Event status: " + arrayList[position].status?.name
        return convertview
    }
}