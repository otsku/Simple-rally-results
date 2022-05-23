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

/**
 * Custom adapter to show rally results after stage
 *
 * @author Otto Kujala
 *
 */
class MyStageAdapter(context: Context, resource: Int, private val arrayList: ArrayList<Result>, private val drivers: ArrayList<Entrant?>) : ArrayAdapter<Result>(context, resource, arrayList) {
    private lateinit var eventcount: TextView
    private lateinit var name: TextView
    private lateinit var diffTime: TextView
    private lateinit var totalTime: TextView
    private lateinit var classView: TextView
    private lateinit var manifacturer: TextView
    private var functions: Functions = Functions()

    /**
     * Add Stage Result and Entrant to list
     * @param item Stage object
     * @param driver Entrant object
     */
    fun add(item: Result, driver: Entrant?) {
        arrayList.add(item)
        drivers.add(driver)
    }

    /**
     * get List of Results
     * @return List of Results
     */
    fun getList(): ArrayList<Result> {
        return arrayList
    }

    /**
     * get Result
     * @param position position of Result in a list
     * @return Result object
     */
    override fun getItem(position: Int): Result {
        return arrayList[position]
    }

    /**
     * get Result id in list
     * @param position position of Result in a list
     * @return Result id in list
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    /**
     * get Result id in list
     * @param position position of Result in a list
     * @param convertView view that has been passed
     * @param parent parent view group
     * @return ListView item with data inserted
     */
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
        manifacturer.text = "Car: "+ drivers[position]?.vehicleModel
        return convertview
    }
}