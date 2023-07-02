package com.example.opsc_part2

import Classes.ToolBox
import Classes.WorkEntriesObject
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class Logs : Fragment(R.layout.fragment_logs) {
    private lateinit var linView: LinearLayout
    private lateinit var etStartDatePick: EditText
    private lateinit var etEndDatePick: EditText
    private lateinit var btnSelectCategory: Button
    private lateinit var btnFilter: Button
    private var selectedCategory = String()
    private lateinit var btnClear: Button
    private lateinit var tvCategorySelected: TextView

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)

        try {
            linView = view.findViewById(R.id.linearProjectCards)

            tvCategorySelected = view.findViewById(R.id.tvCategorySelected)

            btnFilter = view.findViewById(R.id.btnFilter)
            btnFilter.setOnClickListener() {
                LoadFilters()
            }

            btnSelectCategory = view.findViewById(R.id.btnSelectCategory)
            btnSelectCategory.setOnClickListener() {
                showCategoryPickerDialog(0) { selectedCategory ->
                    val showCategoryText = "Category: $selectedCategory"
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
                        val textToSet = "$dayOfMonth-${monthOfYear + 1}-$year"
                        etStartDatePick.setText(textToSet)
                    }, year, month, day
                )
                datePickerDialog.show()
            }


            etEndDatePick = view.findViewById(R.id.etEndDate)
            etEndDatePick.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(), { _, year, monthOfYear, dayOfMonth ->
                        val textToSet = "$dayOfMonth-${monthOfYear + 1}-$year"
                        etEndDatePick.setText(textToSet)
                    }, year, month, day
                )
                datePickerDialog.show()
            }

            btnClear = view.findViewById(R.id.btnClear)
            btnClear.setOnClickListener() {
                linView.removeAllViews()
                LoadFilters()
                selectedCategory = String()
                etEndDatePick.text.clear()
                etStartDatePick.text.clear()
            }

            LoadFilters()

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }

        return view
    }

    //============================================================================
    private fun LoadFilters() {
        try {
            linView.removeAllViews()
            var dateFilerBool = false

            if (etEndDatePick.text.isNotEmpty() && etStartDatePick.text.isNotEmpty()) {
                dateFilerBool = true
            }
            val filtered: List<WorkEntriesObject>

            if (selectedCategory.isNotEmpty() && selectedCategory != "None" && dateFilerBool) {
                //Category and date
                filtered = filterDatesAndCategory(
                    ToolBox.WorkEntriesList,
                    etStartDatePick.text.toString(),
                    etEndDatePick.text.toString(),
                    selectedCategory
                )
                populate(filtered)
                return
            } else if (selectedCategory.isNotEmpty() && selectedCategory != "None" && !dateFilerBool) {
                //Only category
                filtered = filterWorkEntries(ToolBox.WorkEntriesList, selectedCategory, null)
                populate(filtered)
                return
            } else if (selectedCategory == "None" && dateFilerBool) {
                //Only Dates
                filtered = filterDates(
                    ToolBox.WorkEntriesList,
                    etStartDatePick.text.toString(),
                    etEndDatePick.text.toString()
                )
                populate(filtered)
                return
            } else {
                //No filter
                filtered = filterWorkEntries(ToolBox.WorkEntriesList, null, null)
                populate(filtered)

                return
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Function to filter work entries based on the provided filters
    fun filterWorkEntries(
        entries: List<WorkEntriesObject>, selectedCategory: String?, selectedTime: String?
    ): List<WorkEntriesObject> {
        return entries.filter { entry ->
            // Check if the selected category is null or matches the entry's category
            val categoryFilter =
                selectedCategory == null || entry.WEActivityCategory == selectedCategory

            // Check if the selected time is null or matches the entry's time
            val timeFilter = selectedTime == null || entry.WEDateEnded.toString() == selectedTime

            //val userFilter = entry.WEUserID == ToolBox.ActiveUserID

            // Return true only if both filters pass
            categoryFilter && timeFilter //&& userFilter
        }
    }

    //============================================================================
    //Load custom cards
    private fun populate(filtered: List<WorkEntriesObject>) {
        try {

            // ----------------- Creating a new card with custom attributes ----------------- //
            for (card in filtered) {
                val customCard = custom_logs_cards(requireContext())
                customCard.setActivityName(card.WEActivityName)
                customCard.setCardColor(card.WEColor)
                customCard.setActivityDuration(card.WEDuration)
                customCard.setActivityEndDate(card.WEDateEnded.toString())
                customCard.setRating(card.WERating)
                customCard.setRatingColor(card.WERating)

                if (card.getSavedImage() != null) {
                    val imgActivity = customCard.findViewById<ImageView>(R.id.imgActivity)
                    imgActivity.scaleType = ImageView.ScaleType.CENTER_CROP
                    imgActivity.setImageBitmap(card.getSavedImage())
                    imgActivity.setOnClickListener {
                        enlargeImage(imgActivity)
                    }
                }

                var imgActivity = customCard.findViewById<ImageView>(R.id.imgActivity)
                imgActivity.setOnClickListener {
                    enlargeImage(imgActivity)
                }

                linView.addView(customCard)
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //filter by category popup
    private fun showCategoryPickerDialog(defaultIndex: Int = 0, callback: (String) -> Unit) {
        try {
            if (selectedCategory == null) {
                selectedCategory = "None"
            }

            val uniqueCategoriesIN =
                ToolBox.CategoryList.filter { it.CategoryUserID == ToolBox.ActiveUserID }
                    .map { it.CategoryName }.distinct()

            val uniqueCategories = listOf<String>("None", *uniqueCategoriesIN.toTypedArray())

            val selectedIndex = uniqueCategories.indexOf(selectedCategory)
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pick a category").setSingleChoiceItems(
                uniqueCategories.toTypedArray(), selectedIndex
            ) { dialog: DialogInterface, which: Int ->
                selectedCategory = uniqueCategories[which]
                tvCategorySelected.text = selectedCategory
                tvCategorySelected.text = selectedCategory
                callback(selectedCategory!!)
                dialog.dismiss()
            }.setCancelable(true)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //Maximise and minimise the image on click
    private fun enlargeImage(imageView: ImageView) {
        try {
            if (imageView.drawable == null) {
                // No image present, don;t do nothing
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

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Method to filter by date range and category
    private fun filterDatesAndCategory(
        workEntries: List<WorkEntriesObject>,
        startDate: String,
        endDate: String,
        selectedCategory: String
    ): List<WorkEntriesObject> {
        val filteredList = mutableListOf<WorkEntriesObject>()
        try {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateTime = dateFormatter.parse(startDate)
            val endDateTime = dateFormatter.parse(endDate)

            for (entry in workEntries) {
                val entryDate = dateFormatter.parse(entry.WEDateEnded)
                if (entryDate in startDateTime..endDateTime && entry.WEActivityCategory == selectedCategory //&& entry.WEUserID == ToolBox.ActiveUserID
                ) {
                    filteredList.add(entry)
                }
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return filteredList
    }

    //============================================================================
    // Method to filter by dates
    private fun filterDates(
        workEntries: List<WorkEntriesObject>, startDate: String, endDate: String
    ): List<WorkEntriesObject> {
        val filteredList = mutableListOf<WorkEntriesObject>()
        try {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateTime = dateFormatter.parse(startDate)
            val endDateTime = dateFormatter.parse(endDate)

            for (entry in workEntries) {
                val entryDate = dateFormatter.parse(entry.WEDateEnded)
                if (entryDate in startDateTime..endDateTime //&& entry.WEUserID == ToolBox.ActiveUserID
                ) {
                    filteredList.add(entry)
                }
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return filteredList
    }
}