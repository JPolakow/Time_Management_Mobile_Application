package com.example.opsc_part2

import Classes.ToolBox
import android.app.TimePickerDialog
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
import java.util.*

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


        min.setOnClickListener{

            showTimePickerDialogMin();

        }

        max.setOnClickListener{

            showTimePickerDialogMax();

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

    //============================================================================
    // Function to show dialog and set text of editText
    private fun showTimePickerDialogMin() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            min.setText(selectedTime)
        }, currentHour, currentMinute, true)

        timePickerDialog.show()
    }

    //============================================================================
    // Function to show dialog and set text of editText
    private fun showTimePickerDialogMax() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
            max.setText(selectedTime)
        }, currentHour, currentMinute, true)

        timePickerDialog.show()
    }



}