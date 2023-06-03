package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.example.opsc_part2.databinding.FragmentSignUpBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddActivity : Fragment(R.layout.fragment_add_activity) {

    private val colorNames = arrayOf(
        "Red",
        "Blue",
        "Green",
        "Yellow",
        "Cyan",
        "Magenta",
        "Orange",
        "Purple",
        "Lime",
        "Teal",
        "Olive",
        "Maroon",
        "Navy",
        "Pink",
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

    //pressables
    private lateinit var ivSubmit: ImageView
    private lateinit var tvClose: TextView

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
        ivSubmit = view.findViewById<ImageView>(R.id.ivSubmit)
        tvClose = view.findViewById<TextView>(R.id.tvClose)
        DescriptionInput = view.findViewById(R.id.etDescription)

        //add goal
        GoalInput.setOnClickListener {
            showPopupFragment()
        }

        //pick color
        ColorInput.setOnClickListener {
            showColorPickerDialog()
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
            ColorInput.setError("Surname is required")
            valid = false
        }
        if (ToolBox.StartDate.equals("") || ToolBox.EndDate.equals("")) {
            GoalInput.setError("Surname is required")
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
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val current = LocalDateTime.now().format(formatter)
        //get external data
        val currentUser = ToolBox.ActiveUserID
        val activityID = (ToolBox.ActivitiesList.count() + 1)
        //get user inputs
        val name = NameInput.text.toString().trim()

        val newActitivy =
            ActivityObject(activityID, currentUser, name, current, "2", "4", SelectedColor)
        ToolBox.ActivitiesList.add(newActitivy)
    }

    //============================================================================
    //color picker
    private fun showColorPickerDialog() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a color")
            .setItems(colorNames) { dialog: DialogInterface, which: Int ->
                val selectedColor = which

                SelectedColor = colorNames[selectedColor]
                ColorInput.setText(SelectedColor)

                dialog.dismiss()
            }
            .setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    //============================================================================
    //calls the setgoal popup
    private fun showPopupFragment() {
        val fragment = SetGoal()
        fragment.show(childFragmentManager, "QuickActionPopup")
    }
}