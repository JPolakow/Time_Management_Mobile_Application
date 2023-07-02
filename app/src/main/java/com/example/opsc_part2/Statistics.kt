package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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

            //start of chart
            //region
            pieChart = view.findViewById(R.id.chart)
            initPieChart()
            loadPieChartData()
//
//            // Listener for double tap on the pie chart
//            val gestureDetector =
//                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
//                    override fun onDoubleTap(event: MotionEvent): Boolean {
//                        initGameWheel()
//                        loadWheelGameData()
//                        return true
//                    }
//                })
//
//            pieChart.setOnTouchListener { _, event ->
//                gestureDetector.onTouchEvent(event)
//                true
//            }
            //endregion
            //end of chart

            populate()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //pie starts start
    //region
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
        pieChart.dragDecelerationFrictionCoef = 0.50f
    }

    //============================================================================
    fun initGameWheel() {
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

    //============================================================================
    // Function to load data to game wheel
    private fun loadWheelGameData() {

        // Spin the wheel for 3 seconds
        pieChart.spin(3000, 0f, 360f, Easing.EaseInOutQuad)

        // Generate the HashMap of random colors
        val colorMap = generateRandomColorMap()

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

    //============================================================================
    // Method to round sizes of pie chart pies to whole numbers
    class RoundedValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return value.roundToInt().toString()
        }
    }

    //============================================================================
    // This is where we will load our own data
    private fun loadPieChartData() {

        //get all user specific category names
        val filteredCategories = ToolBox.CategoryList.filter { category ->
            category.CategoryUserID == ToolBox.ActiveUserID
        }

        val entries = mutableListOf<PieEntry>()

        for (category in filteredCategories) {
            // Get the total duration of all work entries with the category name

            val totalDuration = ToolBox.WorkEntriesList.filter {
                it.WEActivityCategory == category.CategoryName //&& it.WEUserID == ToolBox.ActiveUserID
            }.groupBy { it.WEActivityCategory }
                .mapValues { (_, entries) -> entries.sumBy { it.WEDuration.toInt() } }

            val total = totalDuration[category.CategoryName]
            if (total != null) {
                entries.add(
                    PieEntry(
                        total.toFloat(), category.CategoryName
                    )
                )
            }

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
    // Method to generate a random color for the wheel
    private fun generateRandomColor(): Int {
        val random = Random()
        val alpha = 255 // You can adjust the alpha value as per your preference
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        return Color.argb(alpha, red, green, blue)
    }

    //============================================================================
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
//endregion
    //pie charts end

    //============================================================================
    @SuppressLint("SetTextI18n")
    private fun populate() {
        try {
            val filteredCategories = ToolBox.CategoryList.filter { category ->
                category.CategoryUserID == ToolBox.ActiveUserID
            }

            for (card in filteredCategories) {

                val customCard = custom_stats_cards(requireContext())
                customCard.setCategoryName("Category: ${card.CategoryName}")

                val cardDisplay = customCard.findViewById<CardView>(R.id.cardView)
                var expanded = false

                // Get the work entries for the current category
                val workEntriesForCategory = ToolBox.WorkEntriesList.filter {
                    it.WEActivityCategory == card.CategoryName
                }

                val linCard = customCard.findViewById<LinearLayout>(R.id.relCard)

                // Retrieving distinct activity names from
                val distinctActivityNames =
                    workEntriesForCategory.map { it.WEActivityName }.distinct()

                // Add TextViews for each work entry
                for (workEntry in distinctActivityNames) {
                    // Dynamically creating a TextView based on number of work entries

                    // Calculate the total duration for activities with the same name
                    val totalDuration =
                        workEntriesForCategory.filter { it.WEActivityName == workEntry }
                            .sumBy { it.WEDuration.toInt() }

                    //ACTIVITY NAME
                    val entryTextView = TextView(requireContext())

                    entryTextView.text = workEntry

                    // Get the corresponding ActivityObject for the activity name
                    val activityObject = getActivityObjectByName(workEntry)

                    //-----DURATION LEFT
                    val durationTextView = TextView(requireContext())

                    val maxGoal = activityObject?.ActivityMaxGoal

                    //Calculate then display duration left
                    durationTextView.text = "Duration Left: ${
                        calculateDurationLeft(
                            maxGoal!!, totalDuration.toDouble()
                        )
                    }"

                    //-----DURATION WORKED
                    val workedTextView = TextView(requireContext())
                    workedTextView.text = "Duration Worked: ${totalDuration.toDouble()}"

                    //-----MAX GOAL
                    val maxGoalTextView = TextView(requireContext())
                    var maxGoalString = ""

                    val overUnderMax = isDurationGreaterThanMax(
                        activityObject.ActivityMaxGoal, totalDuration.toDouble()
                    )

                    if (overUnderMax) maxGoalString =
                        "Max goal (${activityObject?.ActivityMaxGoal.toString()}) achieved"
                    else maxGoalString =
                        "Max goal (${activityObject?.ActivityMaxGoal.toString()}) not achieved"

                    maxGoalTextView.text = maxGoalString

                    //-----MIN GOAL
                    val minGoalTextView = TextView(requireContext())
                    var minGoalString = ""

                    var overUnderMin = isDurationGreaterThanMin(
                        activityObject?.ActivityMinGoal!!, totalDuration.toDouble()
                    )

                    if (overUnderMin) minGoalString =
                        "Min goal (${activityObject?.ActivityMinGoal.toString()}) achieved"
                    else minGoalString =
                        "Min goal (${activityObject?.ActivityMinGoal.toString()}) not achieved"

                    minGoalTextView.text = minGoalString

                    //-----GENERAL
                    val entryParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    entryParams.marginStart =
                        resources.getDimensionPixelSize(R.dimen.entry_start_margin) + 15

                    val entryParamsActivityName = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    entryParamsActivityName.marginStart =
                        resources.getDimensionPixelSize(R.dimen.entry_start_margin)

                    entryTextView.layoutParams = entryParamsActivityName
                    durationTextView.layoutParams = entryParams
                    workedTextView.layoutParams = entryParams
                    maxGoalTextView.layoutParams = entryParams
                    minGoalTextView.layoutParams = entryParams

                    entryTextView.textSize = 20F
                    entryTextView.setTypeface(null, Typeface.BOLD)
                    maxGoalTextView.textSize = 15F
                    minGoalTextView.textSize = 15F
                    durationTextView.textSize = 18F
                    workedTextView.textSize = 18F

                    entryTextView.paintFlags = entryTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    entryTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.dark_grey
                        )
                    )
                    maxGoalTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.dark_grey
                        )
                    )
                    minGoalTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.dark_grey
                        )
                    )
                    durationTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.dark_grey
                        )
                    )
                    workedTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.dark_grey
                        )
                    )

                    entryTextView.visibility = View.GONE
                    minGoalTextView.visibility = View.GONE
                    maxGoalTextView.visibility = View.GONE
                    durationTextView.visibility = View.GONE
                    workedTextView.visibility = View.GONE

                    // Add the TextView to the LinearLayout inside the custom card view
                    linCard.addView(entryTextView)
                    linCard.addView(workedTextView)
                    linCard.addView(durationTextView)
                    linCard.addView(minGoalTextView)
                    linCard.addView(maxGoalTextView)
                }


                // OnClick for each card to expand
                cardDisplay.setOnClickListener {
                    //hidden
                    if (!expanded) {
                        expanded = true
                        for (i in 0 until linCard.childCount) {
                            val child = linCard.getChildAt(i)
                            child.visibility = View.VISIBLE // Show the entryTextView
                        }
                    } else {
                        //shown
                        expanded = false
                        for (i in 3 until linCard.childCount) {
                            val child = linCard.getChildAt(i)
                            child.visibility = View.GONE // Hide the entryTextView
                        }
                    }
                }

                //get the count of all workEntries with the category name
                val frequencies = ToolBox.WorkEntriesList.count {
                    it.WEActivityCategory == card.CategoryName //&& it.WEUserID == ToolBox.ActiveUserID
                }
                customCard.setCategoryAmount("Work entries: $frequencies")

                // Get the total duration of all work entries with the category name
                val totalDuration = ToolBox.WorkEntriesList.filter {
                    it.WEActivityCategory == card.CategoryName //&& it.WEUserID == ToolBox.ActiveUserID
                }.groupBy { it.WEActivityCategory }
                    .mapValues { (_, entries) -> entries.sumBy { it.WEDuration.toInt() } }

                val total = totalDuration[card.CategoryName]
                if (total != null) {
                    customCard.setCategoryDuration("Total duration: $total")
                } else {
                    customCard.setCategoryDuration("Total duration: 0")
                }

                linView.addView(customCard)
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }


    private fun getActivityObjectByName(activityName: String): ActivityObject? {
        return ToolBox.ActivitiesList.find { it.ActivityName == activityName }
    }

    private fun validateUserDurationLeft(max: Double, totalDuration: Double): String {
        var durationLeft = 0.0
        val displayText: String

        if (max - totalDuration > 0) {
            durationLeft = max - totalDuration
            displayText = "Your not done yet, just $durationLeft minutes left!"

        } else {
            displayText = "Wow, your done!"
        }

        return displayText
    }

    // Function to calculate user study minutes remaining
    private fun calculateDurationLeft(max: Double, totalDuration: Double): Double =
        max - totalDuration

    // Function to return whether or not the user has completed their minimum goal
    private fun isDurationGreaterThanMin(Min: Double, totalDuration: Double): Boolean =
        totalDuration >= Min

    // Function to return whether or not the user has completed their maximum goal
    private fun isDurationGreaterThanMax(Max: Double, totalDuration: Double): Boolean =
        totalDuration >= Max
}

// ----------------------- TO DO ----------------------- //
// Individual duration for Work Entry Activity's
// Min + Max goal for activity