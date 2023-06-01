package com.example.opsc_part2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddActivity.newInstance] factory method to
 * create an instance of this fragment.
 */

class AddActivity : Fragment() {
    private var listener: AddActivityFragmentListener? = null

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
 /*   override fun onEditTextClicked() {
        // Handle the EditText click event here
        // This method will be called when any EditText in the SetGoal fragment is clicked
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)

        val etGoalClick = view.findViewById<EditText>(R.id.etGoal)

        etGoalClick.setOnClickListener {

            showSetGoalFragment()

        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddActivityFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddActivityFragmentListener")
        }
    }
    interface AddActivityFragmentListener {
        fun onAddActivityFragmentRequested(fragment: Fragment)
    }
    private fun showSetGoalFragment() {
        val setGoalFragment = SetGoal()
        (activity as? AddActivityFragmentListener)?.onAddActivityFragmentRequested(setGoalFragment)
    }

   /* override fun onFragmentRequested(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }*/
    private fun showPopup() {
        val fragmentt = SetGoal()
        fragmentt.show(requireActivity().supportFragmentManager, "SetGoal")
    }

   /* override fun onFragmentRequested(fragment: Fragment) {
        // Replace the current fragment in the container with the requested fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = activity as? Dashboard
        // Check if the activity is the Dashboard activity
        if (listener != null) {
            listener.onFragmentRequested(this)
        }
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddActivity.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
interface AddActivityFragmentListener {
    fun onAddActivityFragmentRequested(fragment: Fragment)
}