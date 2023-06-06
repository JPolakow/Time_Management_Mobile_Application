package com.example.opsc_part2

import Classes.ToolBox
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.opsc_part2.databinding.FragmentLogsBinding
import java.util.*

class Logs : Fragment(R.layout.fragment_logs) {
    private lateinit var linView: LinearLayout
    private lateinit var  etStartDatePick: EditText
    private lateinit var etEndDatePick: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)

        etStartDatePick = view.findViewById(R.id.etStartDate)

        etStartDatePick.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    etStartDatePick.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        etEndDatePick = view.findViewById(R.id.etEndDate)

        etStartDatePick.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    etEndDatePick.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        linView = view.findViewById(R.id.linearProjectCards)

        populate()

        return view
    }

    private fun populate() {
        // ----------------- Creating a new card with custom attributes ----------------- //
        for (card in ToolBox.WorkEntriesList) {

            val customCard = custom_logs_cards(requireContext())
            customCard.setActivityName(card.WEActivityName)
            customCard.setCardColor(card.WEColor)
            customCard.setActivityDuaration(card.WEDuration)
            customCard.setActivityEndDate(card.WEDateEnded)

            linView.addView(customCard)
        }
    }

    private fun showCategoryPickerDialog(callback: (String) -> Unit) {

        val catagoryNames = mutableListOf<String>()

        for (item in ToolBox.CategoryList) {
            val secondIndexEntry = item.CategoryName
            catagoryNames.add(secondIndexEntry)
        }

        var displaySelected = "Catagory: ";

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a catagory")
            .setItems(catagoryNames.toTypedArray()) { dialog: DialogInterface, which: Int ->
                val selectedCatagory = which

                ToolBox.SelectedCategory = catagoryNames[selectedCatagory]
                displaySelected += ToolBox.SelectedCategory
//                tvCategory.setText(displaySelected)
                callback(ToolBox.SelectedCategory)

                dialog.dismiss()
            }.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }
}