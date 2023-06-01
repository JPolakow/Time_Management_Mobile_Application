package com.example.opsc_part2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SetGoal.newInstance] factory method to
 * create an instance of this fragment.
 */
class SetGoal : BottomSheetDialogFragment() {

    private lateinit var etMinGoal: EditText
    private lateinit var etMaxGoal: EditText
    private lateinit var etWorkingDays: EditText
    lateinit var listener: AddActivityFragmentListener
   // private var editTextClickListener: (() -> Unit)? = null
   private var editTextClickListener: EditTextClickListener? = null
   //private var editTextClickListener: EditTextClickListener? = null
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
        val view = inflater.inflate(R.layout.fragment_set_goal, container, false)

        // Find the buttons in the inflated view
        etMinGoal = view.findViewById(R.id.etTapToSetMinGoal)
        etMaxGoal = view.findViewById(R.id.etSetMaxGoal)
        etWorkingDays = view.findViewById(R.id.etTapWorkingDays)
        // Set click listeners for the EditTexts
        etMinGoal.setOnClickListener {
            editTextClickListener?.onEditTextClicked()
        }

        etMaxGoal.setOnClickListener {
            editTextClickListener?.onEditTextClicked()
        }

        etWorkingDays.setOnClickListener {
            editTextClickListener?.onEditTextClicked()
        }
        return view


    }
    fun setEditTextClickListener(listener: EditTextClickListener) {
        this.editTextClickListener = listener
    }
    interface EditTextClickListener {
        fun onEditTextClicked()
    }

 /*   fun setEditTextClickListener(listener: () -> Unit) {
        editTextClickListener = listener
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SetGoal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SetGoal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddActivityFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddActivityFragmentListener")
        }
    }
}