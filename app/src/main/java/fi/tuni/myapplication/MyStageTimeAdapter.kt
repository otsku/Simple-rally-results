package fi.tuni.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.ArrayList

class MyStageTimeAdapter(context: Context, resource: Int, private val arrayList: ArrayList<StageTime>, private val drivers: ArrayList<Entrant?>) : ArrayAdapter<StageTime>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var diffTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var classView: TextView
    private lateinit var manifacturer: TextView
    private var functions: Functions = Functions()

    fun add(item: StageTime, driver: Entrant?) {
        arrayList.add(item)
        drivers.add(driver)
    }

    fun getList(): ArrayList<StageTime> {
        return arrayList
    }

    override fun getItem(position: Int): StageTime {
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
        val tTime = functions.getTime(arrayList[position].elapsedDurationMs!!)
        totalTime.text = "Total time: $tTime s"
        val dTime = functions.getTime(arrayList[position].diffFirstMs!!)
        diffTime.text = "Diff to first: +$dTime s"
        classView.text = "Group: " + drivers[position]?.group?.name
        manifacturer.text = "Car: "+ drivers[position]?.vehicleModel
        return convertview
    }
}