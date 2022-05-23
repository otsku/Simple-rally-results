package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Custom adapter to show Rally events in a list
 *
 * @author Otto Kujala
 *
 */
class MyAdapter(context: Context, resource: Int, private val arrayList: ArrayList<Items>) : ArrayAdapter<Items>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var status: TextView
    private lateinit var country: TextView

    /**
     * Add Event to list
     * @param item Event object
     */
    fun add(item: Items) {
        arrayList.add(item)
    }

    /**
     * get List of Events
     * @return List of events
     */
    fun getList(): ArrayList<Items> {
        return arrayList
    }

    /**
     * get Event
     * @param position position of event in a list
     * @return Event object
     */
    override fun getItem(position: Int): Items {
        return arrayList[position]
    }

    /**
     * get Event id in list
     * @param position position of event in a list
     * @return Event id in list
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    /**
     * get Event id in list
     * @param position position of event in a list
     * @param convertView view that has been passed
     * @param parent parent view group
     * @return ListView item with data inserted
     */
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