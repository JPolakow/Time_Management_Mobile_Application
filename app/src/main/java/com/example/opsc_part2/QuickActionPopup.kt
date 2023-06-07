package com.example.opsc_part2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QuickActionPopup : BottomSheetDialogFragment() {

    //ui vars
    private lateinit var btnCreateActivity: Button
    private lateinit var btnCreateGroup: Button
    private lateinit var listener: DashboardFragmentListener

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quick_action_popup, container, false)
        try {
            // Find the buttons in the inflated view
            btnCreateActivity = view.findViewById(R.id.btnCreateActivity)
            btnCreateGroup = view.findViewById(R.id.btnCreateCategory)

            // Set click listeners for the buttons
            btnCreateActivity.setOnClickListener {
                val fragment = AddActivity() // Replace with the desired fragment
                listener.onFragmentRequested(fragment)
                dismiss()
            }

            btnCreateGroup.setOnClickListener {
                val fragment = Add_Catagory()
                fragment.show(parentFragmentManager, "QuickActionPopup")
                dismiss()
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //============================================================================
    interface DashboardFragmentListener {
        fun onFragmentRequested(fragment: Fragment)
    }

    //============================================================================
    override fun onAttach(context: Context) {
        try {
            super.onAttach(context)
            if (context is DashboardFragmentListener) {
                listener = context
            } else {
                throw RuntimeException("$context must implement DashboardFragmentListener")
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }
}