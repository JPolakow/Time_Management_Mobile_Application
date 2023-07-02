package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import android.content.ContentValues
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : Fragment(R.layout.fragment_add_activity), SetGoal.GoalPopupListener {

    private val colorNames = arrayOf(
        "Red", "Blue", "Purple", "Pink", "Light-Blue"
    )

    //inputs
    private lateinit var nameInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var colorInput: EditText
    private lateinit var goalInput: EditText
    private lateinit var descriptionInput: EditText
    private var selectedColor: String = ""
    private var selectedCategory: String = ""
    private var minTime = -1
    private var maxTime = -1

    //Press-ables
    private lateinit var ivSubmit: ImageButton
    private lateinit var tvClose: ImageButton

    private var key: String = ""

    //============================================================================
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)

        nameInput = view.findViewById(R.id.etName)
        goalInput = view.findViewById(R.id.etGoal)
        colorInput = view.findViewById(R.id.etColor)
        ivSubmit = view.findViewById(R.id.ivSubmit)
        tvClose = view.findViewById(R.id.ibClose)
        categoryInput = view.findViewById(R.id.etCategory)
        descriptionInput = view.findViewById(R.id.etDescription)

        //Add goal
        goalInput.setOnClickListener {
            showPopupFragment()
        }

        //Pick a color
        colorInput.setOnClickListener {
            showColorPickerDialog()
        }

        //Pick Category
        categoryInput.setOnClickListener {
            showCategoryPickerDialog()
        }

        //Submit Button
        ivSubmit.setOnClickListener() {
            if (validateForm()) {
                addActivityToList() { outcome ->
                    if (outcome) {
                        val intent = Intent(requireActivity(), Dashboard::class.java)
                        // Putting Extra for success message for newly added activity
                        intent.putExtra("successMessage","Successfully Added Activity!")
                        val options =
                            ActivityOptionsCompat.makeCustomAnimation(requireContext(), 0, 0)
                        ActivityCompat.startActivity(requireActivity(), intent, options.toBundle())
                    } else {

                    }
                }
            }
        }

        //Close Button
        tvClose.setOnClickListener() {
            val intent = Intent(requireActivity(), Dashboard::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), 0, 0)
            ActivityCompat.startActivity(requireActivity(), intent, options.toBundle())
        }

        return view
    }

    //============================================================================
    //ensure user has Inputted valid data
    private fun validateForm(): Boolean {
        try {

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        var valid = true
        val name: String = nameInput.text.toString().trim()
        val category: String = categoryInput.text.toString().trim()
        val desc: String = descriptionInput.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            nameInput.error = "Name is required"
            valid = false
        }

        val actIndex = ToolBox.ActivitiesList.indexOfFirst { act -> act.ActivityName == name }
        if (actIndex != -1) {
            nameInput.error = "Name must be unique"
            valid = false
        }

        if (TextUtils.isEmpty(desc)) {
            descriptionInput.error = "Description is required"
            valid = false
        }
        if (TextUtils.isEmpty(category)) {
            categoryInput.error = "Category is required"
            valid = false
        }
        if (selectedColor == "") {
            colorInput.error = "Color is required"
            valid = false
        }
        if (minTime == -1 || maxTime == -1) {
            goalInput.error = "Goal is required"
            valid = false
        }

        return valid
    }

    //============================================================================
    //Add the new entry to the list
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addActivityToList(callback: (Boolean) -> Unit) {
        try {
            // Creating correct date format
            val time = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
            //get external data
            val currentUser = ToolBox.ActiveUserID
            //get user inputs
            val name = nameInput.text.toString().trim()
            val desc = descriptionInput.text.toString().trim()

            val newActivity = ActivityObject(
                "",
                currentUser,
                name,
                selectedCategory,
                time,
                minTime.toDouble(),
                maxTime.toDouble(),
                selectedColor,
                desc,
            )

            //writeToDB callback
            writeToDB(newActivity) { outcome ->
                if (outcome) {
                    newActivity.ActivityID = key
                    ToolBox.ActivitiesList.add(newActivity)
                    callback(true)
                } else {
                    callback(false)
                }
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //save new activity to db
    private fun writeToDB(newActivityInput: ActivityObject, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore

        val newActivity = hashMapOf(
            "ActivityCategory" to newActivityInput.ActivityCategory,
            "ActivityColor" to newActivityInput.ActivityColor,
            "ActivityDescription" to newActivityInput.ActivityDescription,
            "ActivityMaxGoal" to newActivityInput.ActivityMaxGoal,
            "ActivityMinGoal" to newActivityInput.ActivityMinGoal,
            "ActivityName" to newActivityInput.ActivityName,
            "ActivityUserID" to newActivityInput.ActivityUserID,
            "DateCreated" to newActivityInput.DateCreated
        )

        db.collection("activities")
            .add(newActivity)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                key = documentReference.id
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }

    //============================================================================
    //Method to show color picker dialog
    private fun showColorPickerDialog() {
        try {
            var displaySelected = "";

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pick a color")
                .setItems(colorNames) { dialog: DialogInterface, which: Int ->

                    selectedColor = colorNames[which]
                    displaySelected += selectedColor
                    colorInput.setText(displaySelected)

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
    //Category picker
    private fun showCategoryPickerDialog() {
        try {

            val uniqueCategories =
                ToolBox.CategoryList.filter { true }//{ it.CategoryUserID == ToolBox.ActiveUserID }
                    .map { it.CategoryName }.distinct()

            var displaySelected = "";

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Pick a category")
                .setItems(uniqueCategories.toTypedArray()) { dialog: DialogInterface, which: Int ->

                    selectedCategory = uniqueCategories[which]
                    displaySelected += selectedCategory
                    categoryInput.setText(displaySelected)

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
    //Calls the set goal popup
    private fun showPopupFragment() {
        val fragment = SetGoal()
        fragment.show(childFragmentManager, "QuickActionPopup")
    }

    //============================================================================
    //When the set goal is completed it will return values
    override fun onGoalSubmitted(minGoal: Int, maxGoal: Int) {
        maxTime = maxGoal
        minTime = minGoal

        val maxHours = maxGoal / 60
        val maxMinutes = maxGoal % 60
        val minHours = minGoal / 60
        val minMinutes = minGoal % 60

        val maxOutput = String.format("%02d:%02d", maxHours, maxMinutes)
        val minOutput = String.format("%02d:%02d", minHours, minMinutes)

        val textToSet = "Min time: ${minOutput}\t Max time: $maxOutput"
        goalInput.setText(textToSet)
    }
}