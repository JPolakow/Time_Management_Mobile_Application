package com.example.opsc_part2

import Classes.CategoryObject
import Classes.ToolBox
import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Add_Catagory : BottomSheetDialogFragment() {

    // UI Late init Variables
    private lateinit var btnAddCategory: Button
    private lateinit var etCategoryInput: EditText

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add__catagory, container, false)
        try {
            // Find buttons in the inflated view
            btnAddCategory = view.findViewById(R.id.btnAddCategory)
            etCategoryInput = view.findViewById(R.id.etCategoryInput)

            // Set click listeners for the buttons
            btnAddCategory.setOnClickListener {
                validateInput()
            }

        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //============================================================================
    // Validate user inputs
    private fun validateInput() {
        var valid = true

        val name = etCategoryInput.text.toString().trim()


        // Ensuring a name is entered
        if (TextUtils.isEmpty(name)) {
            etCategoryInput.error = "Name is required"
            valid = false
        }

        // Ensuring category name is unique
        val catIndex = ToolBox.CategoryList.indexOfFirst { act -> act.CategoryName == name }
        if (catIndex != -1) {
            etCategoryInput.error = "Name must be unique"
            valid = false
        }

        if (valid) {
            // Add category if the input was all valid
            addNewCategory()
        }
    }

    //============================================================================
    // Add the new category to the local list and the db
    private fun addNewCategory() {
        val newCategory =
            CategoryObject(etCategoryInput.text.toString().trim(), ToolBox.ActiveUserID)

        // WriteToDB callback
        writeToDB(newCategory) { outcome ->
            if (outcome) {
                ToolBox.CategoryList.add(newCategory)
            } else {
                // Failure
            }
        }

        dismiss()
    }

    //============================================================================
    // Save new category in db
    private fun writeToDB(newCategoryInput: CategoryObject, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore

        val newCategory = hashMapOf(
            "CategoryName" to newCategoryInput.CategoryName,
            "CategoryUserID" to newCategoryInput.CategoryUserID
        )

        db.collection("categories").add(newCategory).addOnSuccessListener { documentReference ->
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            callback(true)
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error adding document", e)
            callback(false)
        }
    }
}