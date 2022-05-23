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

class MyStageAdapter(context: Context, resource: Int, private val arrayList: ArrayList<Result>, private val drivers: ArrayList<Entrant?>, private val times: ArrayList<StageTime?>) : ArrayAdapter<Result>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var diffTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var stageTime: TextView
    private lateinit var classView: TextView
    private lateinit var manifacturer: TextView

    fun add(item: Result, driver: Entrant?, time: StageTime?) {
        arrayList.add(item)
        drivers.add(driver)
        times.add(time)
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
        stageTime = convertview.findViewById(R.id.stageTextView)
        classView = convertview.findViewById(R.id.classTextView)
        manifacturer = convertview.findViewById(R.id.manifacturerTextView)
        eventcount.text = arrayList[position].position.toString()
        name.text = drivers[position]?.driver?.fullName.toString()
        val tTime = getTime(arrayList[position].totalTimeMs!!)
        totalTime.text = "Total time: $tTime s"
        val sTime = getTime(times[position]?.elapsedDurationMs!!)
        stageTime.text = "Stage time: $sTime s"
        val dTime = getTime(arrayList[position].diffFirstMs!!)
        diffTime.text = "Diff to first: +$dTime s"
        classView.text = "Group: " + drivers[position]?.group?.name
        manifacturer.text = "Manifacturer: "+ drivers[position]?.manufacturer?.name
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