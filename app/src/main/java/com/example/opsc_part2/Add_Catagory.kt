package com.example.opsc_part2

import Classes.CatagoryObject
import Classes.ToolBox
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Add_Catagory : BottomSheetDialogFragment() {

    //ui vars
    private lateinit var btnAddCatagory: Button
    private lateinit var etCatagoryInput: EditText

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add__catagory, container, false)

        // Find the buttons in the inflated view
        btnAddCatagory = view.findViewById(R.id.btnAddCatagory)
        etCatagoryInput = view.findViewById(R.id.etCatagoryInput)

        // Set click listeners for the buttons
        btnAddCatagory.setOnClickListener {
            if (!etCatagoryInput.text.toString().trim().equals("")) {
                var catagory = CatagoryObject(etCatagoryInput.text.toString().trim(), ToolBox.ActiveUserID)
                ToolBox.CatagoryList.add(catagory)
                dismiss()
            }
        }
        return view
    }
}