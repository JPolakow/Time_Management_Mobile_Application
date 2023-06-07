package com.example.opsc_part2

import Classes.CategoryObject
import Classes.ToolBox
import Classes.WorkEntriesObject
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

class Statistics : Fragment(R.layout.fragment_statistics) {

    private lateinit var linView: LinearLayout

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        try {
            linView = view.findViewById(R.id.linearProjectCards)
            populate()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //============================================================================
    private fun populate() {
        try {
            val filteredCatagories = ToolBox.CategoryList.filter { activity ->
                activity.CategoryUserID == ToolBox.ActiveUserID
            }

            for (card in filteredCatagories) {
                val customCard = custom_stats_cards(requireContext())
                customCard.setCatagoryName("Name: ${card.CategoryName}")

                //get the count of all workentries with the catagory name
                val frequencies =
                    ToolBox.WorkEntriesList.count { it.WEActivityCategory == card.CategoryName }
                customCard.setCatagoryAmount("Work entries: $frequencies")

                //get the toal duration of all work entries with the catagory name
                val totalDuration =
                    ToolBox.WorkEntriesList.filter { it.WEActivityCategory == card.CategoryName }
                        .groupBy { it.WEActivityCategory }
                        .mapValues { (_, entries) -> entries.sumBy { it.WEDuration.toInt() } }

                val total = totalDuration[card.CategoryName]
                if (total != null) {
                    customCard.setCatagoryDuration("Total duration: $total")
                }

                linView.addView(customCard)
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }
}