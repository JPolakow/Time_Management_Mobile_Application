package com.example.opsc_part2

import Classes.ToolBox
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.opsc_part2.databinding.FragmentLogsBinding

class Logs : Fragment(R.layout.fragment_logs) {
    private lateinit var linView: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)

        linView = view.findViewById(R.id.linearProjectCards)

        Populate()

        return view
    }

    private fun Populate() {
        // ----------------- Creating a new card with custom attributes ----------------- //
        var counter = 0
        for (card in ToolBox.WorkEntriesList) {

            val customCard = custom_logs_cards(requireContext())
            customCard.setActivityName(ToolBox.ActivitiesList.get(counter).ActivityName)
            customCard.setCardColor(card.WEColor)
            customCard.setActivityDuaration(card.WEDuration)
            customCard.setActivityEndDate(card.WEDateEnded)

            linView.addView(customCard)
            counter++

        }
    }
}