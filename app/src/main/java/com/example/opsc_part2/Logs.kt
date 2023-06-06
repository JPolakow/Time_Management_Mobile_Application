package com.example.opsc_part2

import Classes.ToolBox
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.opsc_part2.databinding.FragmentLogsBinding

class Logs : Fragment(R.layout.fragment_logs) {

    private lateinit var linView: LinearLayout
    private lateinit var selectCatagory: TextView
    private var filterSelectedCatagory: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)

        linView = view.findViewById(R.id.linearProjectCards)
        selectCatagory = view.findViewById(R.id.tvLogsHeading)
        selectCatagory.setOnClickListener()
        {
            showCategoryPickerDialog { selectedCategory ->
                selectCatagory.text = "Category: $selectedCategory"
            }
        }

        populate()

        return view
    }

    //============================================================================
    //catagory picker
    private fun showCategoryPickerDialog(callback: (String) -> Unit) {

        val catagoryNames = mutableListOf<String>()

        for (item in ToolBox.CategoryList) {
            val secondIndexEntry = item.CategoryName
            catagoryNames.add(secondIndexEntry)
        }

        var displaySelected = "Catagory: ";

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pick a catagory")
            .setItems(catagoryNames.toTypedArray()) { dialog: DialogInterface, which: Int ->
                val selectedCatagory = which

                filterSelectedCatagory = catagoryNames[selectedCatagory]
                callback(filterSelectedCatagory)

                dialog.dismiss()
            }.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }

    //============================================================================
    //load custom cards
    private fun populate() {
        // ----------------- Creating a new card with custom attributes ----------------- //
        for (card in ToolBox.WorkEntriesList) {

            val customCard = custom_logs_cards(requireContext())
            customCard.setActivityName(card.WEActivityName)
            customCard.setCardColor(card.WEColor)
            customCard.setActivityDuaration(card.WEDuration)
            customCard.setActivityEndDate(card.WEDateEnded)

            if (card.getSavedImage() != null)
            {
                customCard.SetImage(card.getSavedImage()!!)
            }

            var imgActivity = customCard.findViewById<ImageView>(R.id.imgActivity)
            imgActivity.setOnClickListener {
                enlargeImage(imgActivity)
            }

            linView.addView(customCard)
        }
    }

    //============================================================================
    //maximise and minimise the image on click
    private fun enlargeImage(imageView: ImageView) {
        if (imageView.drawable == null) {
            // No image present, do nothing
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
    }
}