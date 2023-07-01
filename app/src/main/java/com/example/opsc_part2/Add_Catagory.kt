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

    //UI Lateinit Vars
    private lateinit var btnAddCategory: Button
    private lateinit var etCategoryInput: EditText

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add__catagory, container, false)
        try {
            // Find buttons in the inflated view
            btnAddCategory = view.findViewById(R.id.btnAddCatagory)
            etCategoryInput = view.findViewById(R.id.etCatagoryInput)

            // Set click listeners for the buttons
            btnAddCategory.setOnClickListener {
                valadateInput()
            }

        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //============================================================================
    //valaidate user inputs
    private fun valadateInput() {
        var valid = true

        val name = etCategoryInput.text.toString().trim()


        if (TextUtils.isEmpty(name)) {
            etCategoryInput.error = "Name is required"
            valid = false
        }

        val catIndex = ToolBox.CategoryList.indexOfFirst { act -> act.CategoryName == name }
        if (catIndex != -1) {
            etCategoryInput.error = "Name must be unique"
            valid = false
        }

        if (valid)
        {
            AddNewCatagory()
        }
    }

    //============================================================================
    //add the new catagory to the local list and the db
    private fun AddNewCatagory() {
        var newCatagory =
            CategoryObject(etCategoryInput.text.toString().trim(), ToolBox.ActiveUserID)

        //writeToDB callback
        writeToDB(newCatagory) { outcome ->
            if (outcome) {
                ToolBox.CategoryList.add(newCatagory)
            } else {
                // Failure
            }
        }

        dismiss()
    }

    //============================================================================
    //save new catagory in db
    private fun writeToDB(newCatagoryInput: CategoryObject, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore

        val newCatagory = hashMapOf(
            "CategoryName" to newCatagoryInput.CategoryName,
            "CategoryUserID" to newCatagoryInput.CategoryUserID
        )

        db.collection("categories")
            .add(newCatagory)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }
}