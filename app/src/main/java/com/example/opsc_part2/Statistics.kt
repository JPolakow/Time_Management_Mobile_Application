package com.example.opsc_part2

import Classes.ToolBox
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class Statistics : Fragment(R.layout.fragment_statistics) {

    private lateinit var linView: LinearLayout
    private lateinit var pieChart: PieChart

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        try {
           linView = view.findViewById(R.id.linearProjectCards)

            pieChart = view.findViewById(R.id.chart)
            initPieChart()
            loadPieChartData()

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
        pieChart.dragDecelerationFrictionCoef = 0.95f
    }

    //============================================================================
    // This is where we will load our own data
    private fun loadPieChartData() {
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(30f, "John"))
        entries.add(PieEntry(40f, "Jake"))
        entries.add(PieEntry(50f, "Peter"))

        val dataSet = PieDataSet(entries, "Sample Pie Chart")
        dataSet.colors = listOf(Color.CYAN, Color.BLUE, Color.MAGENTA)
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }

    //============================================================================
    private fun populate() {
        try {
            val filteredCategories = ToolBox.CategoryList.filter { catagory ->
                catagory.CategoryUserID == ToolBox.ActiveUserID
            }

            for (card in filteredCategories) {
                val customCard = custom_stats_cards(requireContext())
                customCard.setCategoryName("Catagory: ${card.CategoryName}")

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
}