package com.example.opsc_part2

import Classes.ToolBox
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.opsc_part2.databinding.FragmentSetGoalBinding
import com.example.opsc_part2.databinding.FragmentSignUpBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetGoal : BottomSheetDialogFragment(R.layout.fragment_set_goal) {
    //bind the front end, making it accessible
    private var _binding: FragmentSetGoalBinding? = null
    private val binding get() = _binding!!

    private lateinit var submit: ImageButton
    private lateinit var min: EditText
    private lateinit var max: EditText

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetGoalBinding.inflate(inflater, container, false)

        submit = binding.ibSubmit
        min = binding.etMinTime
        max = binding.etMaxTime

        submit.setOnClickListener()
        {
            if (Valadate())
            {
                ToolBox.MinGoal = min.text.toString().toInt()
                ToolBox.MaxGoal = max.text.toString().toInt()
            }
        }

        return binding.root
    }

    //============================================================================
    private fun Valadate(): Boolean
    {
        var valid = true

        if (min.text.toString().trim().equals(""))
        {
            min.setError("Surname is required")
            valid = false
        }
        if (max.text.toString().trim().equals(""))
        {
            max.setError("Surname is required")
            valid = false
        }

        return valid
    }


}