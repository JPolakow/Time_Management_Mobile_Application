package com.example.opsc_part2

import Classes.ToolBox
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.NumberPicker
import com.example.opsc_part2.databinding.FragmentSetGoalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetGoal : BottomSheetDialogFragment(R.layout.fragment_set_goal) {
    //bind the front end, making it accessible
    private var _binding: FragmentSetGoalBinding? = null
    private val binding get() = _binding!!

    private lateinit var submit: ImageButton
    private lateinit var min: EditText
    private lateinit var max: EditText

    private var goalPopupListener: GoalPopupListener? = null

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

        goalPopupListener = parentFragment as? GoalPopupListener

        submit.setOnClickListener()
        {
            if (validate()) {

                val minGoal = formatGoalInput(min.text.toString())
                val maxGoal = formatGoalInput(max.text.toString())

                goalPopupListener?.onGoalSubmitted(minGoal, maxGoal)

                dismiss()
            }
        }

        min.setOnClickListener {
            showTimePickerDialogMin();
        }

        max.setOnClickListener {
            showTimePickerDialogMax();
        }
        return binding.root
    }

    //============================================================================
    interface GoalPopupListener {
        fun onGoalSubmitted(minGoal: Int, maxGoal: Int)
    }


    //============================================================================
    private fun validate(): Boolean {
        try {
            var valid = true

            if (min.text.toString().trim() == "") {
                min.error = "Time is required"
                valid = false
            }

            if (max.text.toString().trim() == "") {
                max.error = "Time is required"
                valid = false
            }
            return valid
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return false
        }
    }

    //============================================================================
    // Function to add values to edit text - forgot the edit text isnt on this fragment
    private fun addTimeToEditText(min: Int, max: Int, goalText: EditText) {
        val textToSet = "Min: $min | Max: $max";
        goalText.setText(textToSet)

    }

    //============================================================================
    private fun formatGoalInput(inputValue: String): Int {
        // Val to store value to split at
        val valToSplit = inputValue.split(":")

        // val to store hours of string
        val hours = valToSplit[0].toInt()

        // val to store minutes of string
        val minutes = valToSplit[1].toInt()

        // val to store total minutes
        val totalMinutes = hours * 60 + minutes

        // returning total minutes
        return totalMinutes
    }

    //============================================================================
    // Function to show dialog and set text of editText
    private fun showTimePickerDialogMin() {
        try {
            //HOURS
            val hours = arrayOf(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
            )
            val hourPicker = NumberPicker(requireContext())
            hourPicker.apply {
                minValue = 0
                maxValue = hours.size - 1
                displayedValues = hours
                wrapSelectorWheel = true
            }

            //MINUTES
            val minutes =
                arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
            val minutePicker = NumberPicker(requireContext())
            minutePicker.apply {
                minValue = 0
                maxValue = minutes.size - 1
                displayedValues = minutes
                wrapSelectorWheel = true
            }

            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.HORIZONTAL
            layout.addView(hourPicker)
            layout.addView(minutePicker)

            val alertDialog = AlertDialog.Builder(requireContext(), R.style.CenteredDialog)
                .setTitle("Select Time")
                .setView(layout)
                .setPositiveButton("OK") { _, _ ->
                    val selectedHour = hours[hourPicker.value]
                    val selectedMinute = minutes[minutePicker.value]
                    val selectedTime = "$selectedHour:$selectedMinute"
                    min.setText(selectedTime)
                    // Use the selectedTime as needed
                }
                .setNegativeButton("Cancel", null)
                .create()

            alertDialog.show()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Function to show dialog and set text of editText
    private fun showTimePickerDialogMax() {
        try {
            //HOURS
            val hours = arrayOf(
                "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
            )
            val hourPicker = NumberPicker(requireContext())
            hourPicker.apply {
                minValue = 0
                maxValue = hours.size - 1
                displayedValues = hours
                wrapSelectorWheel = true
            }

            //MINUTES
            val minutes =
                arrayOf("00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
            val minutePicker = NumberPicker(requireContext())
            minutePicker.apply {
                minValue = 0
                maxValue = minutes.size - 1
                displayedValues = minutes
                wrapSelectorWheel = true
            }

            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.HORIZONTAL
            layout.addView(hourPicker)
            layout.addView(minutePicker)

            val alertDialog = AlertDialog.Builder(requireContext(), R.style.CenteredDialog)
                .setTitle("Select Time")
                .setView(layout)
                .setPositiveButton("OK") { _, _ ->
                    val selectedHour = hours[hourPicker.value]
                    val selectedMinute = minutes[minutePicker.value]
                    val selectedTime = "$selectedHour:$selectedMinute"
                    max.setText(selectedTime)
                    // Use the selectedTime as needed
                }
                .setNegativeButton("Cancel", null)
                .create()

            alertDialog.show()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }
}