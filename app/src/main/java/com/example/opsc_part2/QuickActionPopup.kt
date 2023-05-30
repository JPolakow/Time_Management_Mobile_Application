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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QuickActionPopup.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuickActionPopup : BottomSheetDialogFragment() {

    private lateinit var btnCreateActivity: Button
    private lateinit var btnCreateGroup: Button
    private lateinit var listener: DashboardFragmentListener
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quick_action_popup, container, false)

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
            // Handle Create Group button click
            // Perform the desired action
        }

        return view


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuickActionPopup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuickActionPopup().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    interface DashboardFragmentListener {
        fun onFragmentRequested(fragment: Fragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DashboardFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement DashboardFragmentListener")
        }
    }
}