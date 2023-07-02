package com.example.opsc_part2

import Classes.ActivityObject
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

    private lateinit var trophyimg: ImageView
    private lateinit var trophyname: TextView
    private lateinit var trophyamount: TextView
    private var Amount: Int = 0

    //============================================================================
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achievements_settings, container, false)

        //Front end vars
        trophyname = view.findViewById(R.id.tvTrophyName)
        trophyimg = view.findViewById(R.id.imgTrophy)
        trophyamount = view.findViewById(R.id.tvTrophyAmount)

        //Default values
        trophyname.text = "Max goals achieved"
        trophyimg.setImageResource(R.drawable.trophygrey)
        trophyamount.text = "Amount: $Amount"

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
                Amount++
                trophyimg.setImageResource(R.drawable.trophyincolor)
            }
        }

        trophyamount.text = "Amount: $Amount"

        return view
    }
}
