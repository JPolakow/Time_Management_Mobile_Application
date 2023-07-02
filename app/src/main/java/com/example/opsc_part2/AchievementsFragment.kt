package com.example.opsc_part2

import Classes.ToolBox
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class AchievementsFragment : Fragment() {

    private lateinit var trophyImg: ImageView
    private lateinit var trophyName: TextView
    private lateinit var trophyAmount: TextView
    private var amount: Int = 0

    //============================================================================
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achievements_settings, container, false)

        //Front end vars
        trophyName = view.findViewById(R.id.tvTrophyName)
        trophyImg = view.findViewById(R.id.imgTrophy)
        trophyAmount = view.findViewById(R.id.tvTrophyAmount)

        //Default values
        trophyName.text = "Max goals achieved"
        trophyImg.setImageResource(R.drawable.trophygrey)
        trophyAmount.text = "Amount: $amount"

        //Get lists for user
        val filteredActivity = ToolBox.ActivitiesList.filter { activity ->
            activity.ActivityUserID == ToolBox.ActiveUserID
        }
        val filteredWE = ToolBox.WorkEntriesList.filter { WE ->
            WE.WEUserID == ToolBox.ActiveUserID
        }

        //Find activities with duration over max goal
        for (activity in filteredActivity) {
            val totalDuration =
                filteredWE.filter { it.WEActivityName == activity.ActivityName }
                    .sumBy { it.WEDuration.toInt() }

            if (activity.ActivityMaxGoal <= totalDuration) {
                amount++
                trophyImg.setImageResource(R.drawable.trophyincolor)
            }
        }

        trophyAmount.text = "Amount: $amount"

        return view
    }
}
