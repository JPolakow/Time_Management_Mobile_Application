package com.example.opsc_part2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class AddActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)

        val etGoalClick = view.findViewById<EditText>(R.id.etGoal)
        etGoalClick.setOnClickListener {
            showPopupFragment()
        }

        return view
    }

    private fun showPopupFragment() {
        val popupFragment = SetGoal()
        childFragmentManager.beginTransaction()
            .add(R.id.popup_container, popupFragment)
            .commit()
    }
}