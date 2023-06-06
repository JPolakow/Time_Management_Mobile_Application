package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : Fragment(R.layout.fragment_add_activity), SetGoal.GoalPopupListener {

    private val colorNames = arrayOf(
        "Red",
        "Blue",
        "Purple",
        "Pink",
        "Light-Blue"
    )

    //inputs
    private lateinit var nameInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var colorInput: EditText
    private lateinit var goalInput: EditText
    private lateinit var descriptionInput: EditText
    private var SelectedColor: String = ""
    private var SelectedCatagory: String = ""
    private var minTime = -1
    private var maxTime = -1

    //Press-ables
    private lateinit var ivSubmit: ImageButton
    private lateinit var tvClose: ImageButton

    //============================================================================
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)

        try {

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        nameInput = view.findViewById(R.id.etName)
        goalInput = view.findViewById<EditText>(R.id.etGoal)
        colorInput = view.findViewById<EditText>(R.id.etColor)
        ivSubmit = view.findViewById<ImageButton>(R.id.ivSubmit)
        tvClose = view.findViewById<ImageButton>(R.id.ibClose)
        categoryInput = view.findViewById(R.id.etCategory)



        descriptionInput = view.findViewById(R.id.etName)

        //add goal
        goalInput.setOnClickListener {
            showPopupFragment()
        }

        //pick color
        colorInput.setOnClickListener {
            showColorPickerDialog()
        }

        //pick category
        categoryInput.setOnClickListener {
            showCategoryPickerDialog()
        }

        //submit button
        ivSubmit.setOnClickListener() {
            if (validateForm()) {
                addActivityToList()
                val intent = Intent(requireActivity(), Dashboard::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), 0, 0)
                ActivityCompat.startActivity(requireActivity(), intent, options.toBundle())
            }
        }

        //close button
        tvClose.setOnClickListener() {
            val intent = Intent(requireActivity(), Dashboard::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), 0, 0)
            ActivityCompat.startActivity(requireActivity(), intent, options.toBundle())
        }

        return view
    }

    //============================================================================
    //ensure user has Inputed valid data
    private fun validateForm(): Boolean {
        try {

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        var valid = true
        val name: String = nameInput.getText().toString().trim()
        val catagory: String = categoryInput.getText().toString().trim()


        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required")
            valid = false
        }
        if (TextUtils.isEmpty(catagory)) {
            categoryInput.setError("Catagory is required")
            valid = false
        }
        if (SelectedColor.equals("")) {
            colorInput.setError("Color is required")
            valid = false
        }
        if (minTime == -1 || maxTime == -1) {
            goalInput.setError("Goal is required")
            valid = false
        }

        return valid
    }

    //============================================================================
    //add the new entry to the list
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addActivityToList() {
        try {

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        // Creating correct date format
        val time = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
        //get external data
        val currentUser = ToolBox.ActiveUserID
        val activityID = (ToolBox.ActivitiesList.count() + 1)
        //get user inputs
        val name = nameInput.text.toString().trim()
        val desc = descriptionInput.text.toString().trim()

        val newActitivy =
            ActivityObject(
                activityID,
                currentUser,
                name,
                SelectedCatagory,
                time,
                minTime.toDouble(),
                maxTime.toDouble(),
                SelectedColor,
                desc,
            )
        ToolBox.ActivitiesList.add(newActitivy)
    }

    //============================================================================
    //color picker
    private fun showColorPickerDialog() {
        try {
            var displaySelected = "";

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pick a color")
                .setItems(colorNames) { dialog: DialogInterface, which: Int ->

                    SelectedColor = colorNames[which]
                    displaySelected += SelectedColor
                    colorInput.setText(displaySelected)

                    dialog.dismiss()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.show()
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //catagory picker
    private fun showCategoryPickerDialog() {
        try {
           // val categoryNames = mutableListOf<String>()

            val uniqueCatagories = ToolBox.CategoryList
                .filter { it.CategoryUserID == ToolBox.ActiveUserID }
                .map { it.CategoryName }
                .distinct()

            var displaySelected = "";

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pick a catagory")
                .setItems(uniqueCatagories.toTypedArray()) { dialog: DialogInterface, which: Int ->

                    SelectedCatagory = uniqueCatagories[which]
                    displaySelected += SelectedCatagory
                    categoryInput.setText(displaySelected)

                    dialog.dismiss()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.show()
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //calls the set goal popup
    private fun showPopupFragment() {
        val fragment = SetGoal()
        fragment.show(childFragmentManager, "QuickActionPopup")
    }

    //============================================================================
    //when the set goal is completed it will return values
    override fun onGoalSubmitted(minGoal: Int, maxGoal: Int) {
        minTime = minGoal
        maxTime = maxGoal
    }
}