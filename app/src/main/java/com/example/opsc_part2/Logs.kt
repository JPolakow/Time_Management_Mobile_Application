package com.example.opsc_part2

import Classes.ToolBox
import Classes.WorkEntriesObject
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.example.opsc_part2.databinding.FragmentLogsBinding
import android.app.DatePickerDialog
import android.widget.*
import java.util.*
import kotlin.math.log

class Logs : Fragment(R.layout.fragment_logs) {
    private lateinit var linView: LinearLayout
    private lateinit var etStartDatePick: EditText
    private lateinit var etEndDatePick: EditText
    private lateinit var btnSelectCategory: Button
    private lateinit var btnFilter: Button
    private var SelectedCatagory = String()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)
        linView = view.findViewById(R.id.linearProjectCards)

        btnFilter = view.findViewById(R.id.btnFilter)
        btnFilter.setOnClickListener()
        {
            LoadFilters()
        }

        btnSelectCategory = view.findViewById(R.id.btnSelectCategory)
        btnSelectCategory.setOnClickListener() {
            showCategoryPickerDialog { selectedCategory ->
                val showCategoryText = "Category: $selectedCategory"
                btnSelectCategory.text = showCategoryText
            }
        }

        etStartDatePick = view.findViewById(R.id.etStartDate)
        etStartDatePick.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    etStartDatePick.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                }, year, month, day
            )
            datePickerDialog.show()
        }

        etEndDatePick = view.findViewById(R.id.etEndDate)
        etStartDatePick.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    etEndDatePick.setText("$dayOfMonth-${monthOfYear + 1}-$year")
                }, year, month, day
            )
            datePickerDialog.show()
        }

        LoadFilters()

        return view
    }

    private fun showCategoryPickerDialog(callback: (String) -> Unit) {

        val catagoryNames = mutableListOf<String>()

        catagoryNames.add("None")

        for (item in ToolBox.CategoryList) {
            val secondIndexEntry = item.CategoryName
            catagoryNames.add(secondIndexEntry)
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a catagory")
            .setItems(catagoryNames.toTypedArray()) { dialog: DialogInterface, which: Int ->
                val selectedCatagory = which

                SelectedCatagory = catagoryNames[selectedCatagory]
                callback(SelectedCatagory)

                dialog.dismiss()
            }.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    private fun LoadFilters() {
        linView.removeAllViews()
        var dateFilerBool = false

        if (etEndDatePick.text.length > 0 && etStartDatePick.text.length > 0) {
            dateFilerBool = true
            Log.w(
                "aa",
                "AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"
            )
        }
        val filtered: List<WorkEntriesObject>

        if (!SelectedCatagory.isEmpty() && !SelectedCatagory.equals("None") && dateFilerBool) {
            //catagory and date
            filtered = filterWorkEntries(ToolBox.WorkEntriesList, SelectedCatagory, null)
            populate(filtered)
            return
        } else if (!SelectedCatagory.isEmpty() && !SelectedCatagory.equals("None") && dateFilerBool == false) {
            //only catagory
            filtered = filterWorkEntries(ToolBox.WorkEntriesList, SelectedCatagory, null)
            populate(filtered)
            return
        } else if (SelectedCatagory.isEmpty() && SelectedCatagory.equals("None") && dateFilerBool) {
            //only timer
            filtered = filterWorkEntries(ToolBox.WorkEntriesList, null, null)
            populate(filtered)
            return
        } else {
            //no filter
            filtered = filterWorkEntries(ToolBox.WorkEntriesList, null, null)
            populate(filtered)
            return
        }

    }

    // Function to filter work entries based on the provided filters
    fun filterWorkEntries(
        entries: List<WorkEntriesObject>, selectedCategory: String?, selectedTime: String?
    ): List<WorkEntriesObject> {
        return entries.filter { entry ->
            Log.w("Log", "1")
            // Check if the selected category is null or matches the entry's category
            val categoryFilter =
                selectedCategory == null || entry.WEActivityCategory == selectedCategory
            Log.w("Log", "2")
            // Check if the selected time is null or matches the entry's time
            val timeFilter = selectedTime == null || entry.WEDateEnded == selectedTime
            Log.w("Log", "3")
            // Return true only if both filters pass
            categoryFilter && timeFilter
        }
    }


    //============================================================================
    //load custom cards
    private fun populate(filtered: List<WorkEntriesObject>) {
        // ----------------- Creating a new card with custom attributes ----------------- //
        Log.w("Log", "4")
        for (card in filtered) {
            Log.w("Log", "5")
            val customCard = custom_logs_cards(requireContext())
            customCard.setActivityName(card.WEActivityName)
            customCard.setCardColor(card.WEColor)
            customCard.setActivityDuaration(card.WEDuration)
            customCard.setActivityEndDate(card.WEDateEnded)

            Log.w("Log", "6")
            if (card.getSavedImage() != null) {
                customCard.SetImage(card.getSavedImage()!!)
            }

            Log.w("Log", "7")
            var imgActivity = customCard.findViewById<ImageView>(R.id.imgActivity)
            imgActivity.setOnClickListener {
                enlargeImage(imgActivity)
            }

            Log.w("Log", "8")
            linView.addView(customCard)
        }
    }

    //============================================================================
    //maximise and minimise the image on click
    private fun enlargeImage(imageView: ImageView) {
        if (imageView.drawable == null) {
            // No image present, do nothing
            return
        }

        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.enlarged_image)

        val enlargedImageView = dialog.findViewById<ImageView>(R.id.enlargedImageView)
        enlargedImageView.setImageDrawable(imageView.drawable)
        enlargedImageView.scaleType = ImageView.ScaleType.FIT_CENTER

        enlargedImageView.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}