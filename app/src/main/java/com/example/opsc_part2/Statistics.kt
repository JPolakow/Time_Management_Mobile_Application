package com.example.opsc_part2

import Classes.ToolBox
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*
import kotlin.math.roundToInt


class Statistics : Fragment(R.layout.fragment_statistics) {

    private lateinit var linView: LinearLayout
    private lateinit var pieChart: PieChart

    //============================================================================
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        try {
           linView = view.findViewById(R.id.linearProjectCards)

            pieChart = view.findViewById(R.id.chart)
            initPieChart()
            loadPieChartData()

            // Listener for double tap on the pie chart
            val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(event: MotionEvent): Boolean {
                    initGameWheel()
                    loadWheelGameData()
                    return true
                }
            })

            pieChart.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)
                true
            }

            populate()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }


    //============================================================================
    // Method to initialise pie chart
    // All pie chart properties will go here
    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        // setting pie chart description visibility
        pieChart.description.isEnabled = false
        // setting legend visibility
        pieChart.legend.isEnabled = true
        pieChart.setDrawEntryLabels(false)
        // setting rotation of pie chart = true
        pieChart.isRotationEnabled = true
        // Enabling pie chart hole
        pieChart.isDrawHoleEnabled = true
        // Setting hole size of pie chart
        pieChart.holeRadius = 0f
        pieChart.setHoleColor(android.R.color.transparent)
        // setting drag friction for dragging pie chart
        pieChart.dragDecelerationFrictionCoef = 0.99f
    }

    fun initGameWheel ()
    {
        pieChart.setUsePercentValues(true)
        // setting pie chart description visibility
        pieChart.description.isEnabled = false
        // setting legend visibility
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        // setting rotation of pie chart = true
        pieChart.isRotationEnabled = true
        // Enabling pie chart hole
        pieChart.isDrawHoleEnabled = true
        // Setting hole size of pie chart
        pieChart.holeRadius = 0f
        pieChart.setHoleColor(android.R.color.transparent)
        // setting drag friction for dragging pie chart
        pieChart.dragDecelerationFrictionCoef = 0.99f

    }

    // Function to load data to game wheel
    private fun loadWheelGameData() {
        pieChart.spin(3000, 0f, 360f, Easing.EaseInOutQuad) // Spin the wheel for 3 seconds
        val colorMap = generateRandomColorMap() // Generate the HashMap of random colors

        val entries = mutableListOf<PieEntry>()

        // Generate 7 PieEntries with random values
        for (i in 1..7) {
            val value = (1..100).random().toFloat()
            entries.add(PieEntry(value, "Entry $i"))
        }

        val dataSet = PieDataSet(entries, "Game Wheel")

        // Set the colors for each entry in the dataset using the color map
        val colors = mutableListOf<Int>()
        for (i in entries.indices) {
            val color = colorMap[i % colorMap.size] ?: Color.BLACK
            colors.add(color)
        }
        dataSet.colors = colors

        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        data.setValueFormatter(RoundedValueFormatter()) // Apply custom value formatter
        pieChart.data = data
        pieChart.invalidate()
    }

    // Method to round sizes of pie chart pies to whole numbers
    class RoundedValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.roundToInt().toString()
        }
    }


    //============================================================================
    // This is where we will load our own data
    private fun loadPieChartData() {
//        val entries = mutableListOf<PieEntry>()
//        entries.add(PieEntry(30f, "John"))
//        entries.add(PieEntry(40f, "Jake"))
//        entries.add(PieEntry(50f, "Peter"))
//
//        val dataSet = PieDataSet(entries, "Sample Pie Chart")
//        dataSet.colors = listOf(Color.CYAN, Color.BLUE, Color.MAGENTA)
//        dataSet.valueTextColor = Color.BLACK
//        dataSet.valueTextColor = Color.BLACK
//        dataSet.valueTextSize = 12f
//
//        val data = PieData(dataSet)
//        pieChart.data = data
//        // refresh chart
//        pieChart.invalidate()

        //get all user specific catagory names
        val filteredCategories = ToolBox.CategoryList.filter { category ->
            category.CategoryUserID == ToolBox.ActiveUserID
        }

        val entries = mutableListOf<PieEntry>()

        for (catagory in filteredCategories)
        {
            // Get the total duration of all work entries with the category name
            val totalDuration =
                ToolBox.WorkEntriesList.filter { it.WEActivityCategory == catagory.CategoryName && it.WEUserID == ToolBox.ActiveUserID}
                    .groupBy { it.WEActivityCategory }
                    .mapValues { (_, entries) -> entries.sumBy { it.WEDuration.toInt() } }

            entries.add(PieEntry(totalDuration[catagory.CategoryName]!!.toFloat(), catagory.CategoryName))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.CYAN, Color.BLUE, Color.MAGENTA)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        pieChart.data = data
        // refresh chart
        pieChart.invalidate()
    }

    //============================================================================
    private fun populate() {
        try {
            val filteredCategories = ToolBox.CategoryList.filter { category ->
                category.CategoryUserID == ToolBox.ActiveUserID
            }

            for (card in filteredCategories) {
                val customCard = custom_stats_cards(requireContext())
                customCard.setCategoryName("Category: ${card.CategoryName}")

                //get the count of all workEntries with the category name
                val frequencies =
                    ToolBox.WorkEntriesList.count { it.WEActivityCategory == card.CategoryName && it.WEUserID == ToolBox.ActiveUserID }
                customCard.setCategoryAmount("Work entries: $frequencies")

                // Get the total duration of all work entries with the category name
                val totalDuration =
                    ToolBox.WorkEntriesList.filter { it.WEActivityCategory == card.CategoryName && it.WEUserID == ToolBox.ActiveUserID}
                        .groupBy { it.WEActivityCategory }
                        .mapValues { (_, entries) -> entries.sumBy { it.WEDuration.toInt() } }

                val total = totalDuration[card.CategoryName]
                if (total != null) {
                    customCard.setCategoryDuration("Total duration: $total")
                }

                linView.addView(customCard)
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }


    // Method to generate a random color for the wheel
    private fun generateRandomColor(): Int {
        val random = Random()
        val alpha = 255 // You can adjust the alpha value as per your preference
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        return Color.argb(alpha, red, green, blue)
    }

    // Method to generate a hashmap of random colors
    private fun generateRandomColorMap(): HashMap<Int, Int> {
        val colorMap = HashMap<Int, Int>()
        val numberOfColors = 7 // Adjust the number of colors as per your requirement

        for (i in 0 until numberOfColors) {
            val color = generateRandomColor()
            colorMap[i] = color
        }

        return colorMap
    }

}