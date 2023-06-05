package com.example.opsc_part2

import Classes.ActivityObject
import Classes.CatagoryObject
import Classes.ToolBox
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.opsc_part2.databinding.FragmentSignUpBinding
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : Fragment(R.layout.fragment_add_activity) {

    private val colorNames = arrayOf(
        "Red",
        "Blue",
        "Purple",
        "Pink",
        "Light-Blue"
    )

    //bind the front end, making it accessible
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    //inputs
    private lateinit var NameInput: EditText
    private lateinit var CatagoryInput: EditText
    private lateinit var ColorInput: EditText
    private lateinit var GoalInput: EditText
    private lateinit var DescriptionInput: EditText
    private var SelectedColor: String = ""
    private var SelectedCatagory: String = ""

    //pressables
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

        NameInput = view.findViewById(R.id.etName)
        GoalInput = view.findViewById<EditText>(R.id.etGoal)
        ColorInput = view.findViewById<EditText>(R.id.etColor)
        ivSubmit = view.findViewById<ImageButton>(R.id.ivSubmit)
        tvClose = view.findViewById<ImageButton>(R.id.ibClose)
        CatagoryInput = view.findViewById(R.id.etCategory)

        //add goal
        GoalInput.setOnClickListener {
            showPopupFragment()
        }

        //pick color
        ColorInput.setOnClickListener {
            showColorPickerDialog()
        }

        //pick category
        CatagoryInput.setOnClickListener {
            showCategoryPickerDialog()
        }

        //submit button
        ivSubmit.setOnClickListener() {
            if (validateForm()) {
                AddActivityToList()
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
    //ensure user has inputed valid data
    private fun validateForm(): Boolean {
        var valid = true
        val name: String = NameInput.getText().toString().trim()
        val catagory: String = CatagoryInput.getText().toString().trim()


        if (TextUtils.isEmpty(name)) {
            NameInput.setError("Name is required")
            valid = false
        }
        if (TextUtils.isEmpty(catagory)) {
            CatagoryInput.setError("Catagory is required")
            valid = false
        }
        if (SelectedColor.equals("")) {
            ColorInput.setError("Color is required")
            valid = false
        }
        if (ToolBox.MinGoal.equals("") || ToolBox.MaxGoal.equals("")) {
            GoalInput.setError("Goal is required")
            valid = false
        }

        //add icon

        return valid
    }

    //============================================================================
    //add the new entry to the list
    @RequiresApi(Build.VERSION_CODES.O)
    private fun AddActivityToList() {

        // Creating correct date format
        val time = SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(Date())
        //get external data
        val currentUser = ToolBox.ActiveUserID
        val activityID = (ToolBox.ActivitiesList.count() + 1)
        //get user inputs
        val name = NameInput.text.toString().trim()
        val catagory = CatagoryInput.text.toString().trim()

        val newActitivy =
            ActivityObject(
                activityID,
                currentUser,
                name,
                time,
                catagory,
                ToolBox.MinGoal,
                ToolBox.MaxGoal,
                SelectedColor
            )
        ToolBox.ActivitiesList.add(newActitivy)

        ToolBox.MinGoal = 1
        ToolBox.MaxGoal = 1
    }

    //============================================================================
    //color picker
    private fun showColorPickerDialog() {

        var displaySelected = "Color: ";

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a color")
            .setItems(colorNames) { dialog: DialogInterface, which: Int ->
                val selectedColor = which

                SelectedColor = colorNames[selectedColor]
                displaySelected += SelectedColor
                ColorInput.setText(displaySelected)

                dialog.dismiss()
            }
            .setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    //============================================================================
    //catagory picker
    private fun showCategoryPickerDialog() {

        val catagoryNames = mutableListOf<String>()

        for (item in ToolBox.CatagoryList) {
            val secondIndexEntry = item.CatagoryName
            catagoryNames.add(secondIndexEntry)
        }

        var displaySelected = "Catagory: ";

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a catagory")
            .setItems(catagoryNames.toTypedArray()) { dialog: DialogInterface, which: Int ->
                val selectedCatagory = which

                SelectedCatagory = catagoryNames[selectedCatagory]
                displaySelected += SelectedCatagory
                CatagoryInput.setText(displaySelected)

                dialog.dismiss()
            }
            .setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    //============================================================================
    //calls the set goal popup
    private fun showPopupFragment() {
        val fragment = SetGoal()
        fragment.show(childFragmentManager, "QuickActionPopup")
    }
}