package com.example.opsc_part2

import Classes.CategoryObject
import Classes.ToolBox
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
                if (etCategoryInput.text.toString().trim() != "") {
                    var catagory =
                        CategoryObject(etCategoryInput.text.toString().trim(), ToolBox.ActiveUserID)
                    ToolBox.CategoryList.add(catagory)
                    dismiss()
                }
            }
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }
}