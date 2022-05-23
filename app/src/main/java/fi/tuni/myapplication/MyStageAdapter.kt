package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MyStageAdapter(context: Context, resource: Int, private val arrayList: ArrayList<Result>, private val drivers: ArrayList<Entrant?>) : ArrayAdapter<Result>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var diffTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var classView: TextView
    private lateinit var manifacturer: TextView
    private var functions: Functions = Functions()

    fun add(item: Result, driver: Entrant?) {
        arrayList.add(item)
        drivers.add(driver)
    }

    fun getList(): ArrayList<Result> {
        return arrayList
    }

    override fun getItem(position: Int): Result {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertview = convertView
        convertview = LayoutInflater.from(context).inflate(R.layout.stage_item, parent, false)
        eventcount = convertview.findViewById(R.id.positionTextView)
        name = convertview.findViewById(R.id.nameTextView)
        diffTime = convertview.findViewById(R.id.diffTextView)
        totalTime = convertview.findViewById(R.id.totalTextView)
        classView = convertview.findViewById(R.id.classTextView)
        manifacturer = convertview.findViewById(R.id.manifacturerTextView)
        eventcount.text = arrayList[position].position.toString()
        name.text = drivers[position]?.driver?.fullName.toString()
        val tTime = functions.getTime(arrayList[position].totalTimeMs!!)
        totalTime.text = "Total time: $tTime s"
        val dTime = functions.getTime(arrayList[position].diffFirstMs!!)
        diffTime.text = "Diff to first: +$dTime s"
        classView.text = "Group: " + drivers[position]?.group?.name
        manifacturer.text = "Manifacturer: "+ drivers[position]?.manufacturer?.name
        return convertview
    }
}