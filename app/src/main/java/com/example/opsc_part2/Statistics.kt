package com.example.opsc_part2

import Classes.CategoryObject
import Classes.ToolBox
import Classes.WorkEntriesObject
import android.os.Bundle
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        linView = view.findViewById(R.id.linearProjectCards)
        populate()
        return view
    }

    //============================================================================
    private fun populate() {
        // ----------------- Creating a new card with custom attributes ----------------- //
//        for (card in ToolBox.CategoryList) {
//            val customCard = custom_stats_cards(requireContext())
//            customCard.setCatagoryName(card.CategoryName)
//
//            val frequencies = ToolBox.ActivitiesList.count{it.ActivityCategory == card.CategoryName}
//            customCard.setCatagoryAmount(frequencies.toString())
//
//            val totalDuration = ToolBox.WorkEntriesList
//                .filter { it.WEActivityCategory == card.CategoryName }
//                .groupBy { it.WEActivityCategory }
//                .mapValues { (_, entries) -> entries.sumBy { it.WEDuration } }
//
//            val total = totalDuration[card.CategoryName]
//            if (total != null) {
//                customCard.setCatagoryDuration(total.toString())
//            }
//
//            linView.addView(customCard)
//        }
    }
}