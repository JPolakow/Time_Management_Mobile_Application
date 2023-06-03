package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.app.AlertDialog
import com.example.opsc_part2.databinding.FragmentSignUpBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddActivity : Fragment(R.layout.fragment_add_activity) {

    private val colors = arrayOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.CYAN,
        Color.MAGENTA,
        Color.WHITE,
        Color.rgb(255, 165, 0), // Orange
        Color.rgb(128, 0, 128), // Purple
        Color.rgb(0, 128, 0), // Lime
        Color.rgb(0, 128, 128), // Teal
        Color.rgb(128, 128, 0), // Olive
        Color.rgb(128, 0, 0), // Maroon
        Color.rgb(0, 0, 128), // Navy
        Color.rgb(0, 255, 0), // Fuchsia
        Color.rgb(0, 0, 0) // Aqua
    )
    private val colorNames = arrayOf(
        "Red",
        "Blue",
        "Green",
        "Yellow",
        "Cyan",
        "Magenta",
        "White",
        "Orange",
        "Purple",
        "Lime",
        "Teal",
        "Olive",
        "Maroon",
        "Navy",
        "Fuchsia",
        "Aqua"
    )

    //bind the front end, making it accessible
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    //inputs
    private lateinit var NameInput: EditText
    private lateinit var DateInput: EditText
    private lateinit var CatagoryInput: EditText
    private lateinit var MinGaol: EditText
    private lateinit var MaxGoal: EditText
    private var SelectedColor: Int = -1

    //pressables
    private lateinit var imgAdd: ImageView

    //============================================================================
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)

        NameInput = view.findViewById(R.id.etName)
        CatagoryInput = view.findViewById(R.id.etCategory)


        //add goal
        val etGoalClick = view.findViewById<EditText>(R.id.etGoal)
        etGoalClick.setOnClickListener {
            showPopupFragment()
        }

        //pick color
        val etPickColor = view.findViewById<EditText>(R.id.etColor)
        etPickColor.setOnClickListener {
            showColorPickerDialog()
        }

        //submit button
        val ivSubmit = view.findViewById<ImageView>(R.id.ivSubmit)
        ivSubmit.setOnClickListener() {
            if (validateForm())
                AddActivityToList()
        }

        return view
    }

    //============================================================================
    //ensure user has inputed valid data
    private fun validateForm(): Boolean {
        var valid = true
        val name: String = NameInput.getText().toString().trim()
        val catagory: String = CatagoryInput.getText().toString().trim()

        //user text inputs
        if (TextUtils.isEmpty(name)) {
            NameInput.setError("Name is required")
            valid = false
        }
        if (TextUtils.isEmpty(catagory)) {
            CatagoryInput.setError("Catagory is required")
            valid = false
        }

//        //user selects
//        if (SelectedColor == -1) {
//            DateInput.setError("Surname is required")
//            valid = false
//        }
        //add icon
        //NEED TO VALADATE GOAL

        return valid
    }

    //============================================================================
    //add the new entry to the list
    @RequiresApi(Build.VERSION_CODES.O)
    private fun AddActivityToList() {
        // Creating correct date format
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val current = LocalDateTime.now().format(formatter)
        //get external data
        val currentUser = ToolBox.ActiveUserID
        val activityID = (ToolBox.ActivitiesList.count() + 1)
        //get user inputs
        val name = NameInput.text.toString().trim()

        val newActitivy = ActivityObject(activityID, currentUser, name, current, "2", "4")
        ToolBox.ActivitiesList.add(newActitivy)
    }

    //============================================================================
    //color picker
    private fun showColorPickerDialog() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a color")
            .setItems(colorNames) { dialog: DialogInterface, which: Int ->
                val selectedColor = which

                SelectedColor = colors[selectedColor]

                dialog.dismiss()
            }
            .setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    //============================================================================
    //
    private fun showPopupFragment() {
        val fragment = SetGoal()
        fragment.show(childFragmentManager, "QuickActionPopup")
    }
}