package com.example.opsc_part2

import Classes.ToolBox
import android.app.Dialog
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
import com.example.opsc_part2.databinding.FragmentLogsBinding

class Logs : Fragment(R.layout.fragment_logs) {
    private lateinit var linView: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)

        linView = view.findViewById(R.id.linearProjectCards)

        populate()

        return view
    }

    private fun populate() {
        // ----------------- Creating a new card with custom attributes ----------------- //
        for (card in ToolBox.WorkEntriesList) {

            Log.w("log", "adding card")

            val customCard = custom_logs_cards(requireContext())
            customCard.setActivityName(card.WEActivityName)
            customCard.setCardColor(card.WEColor)
            customCard.setActivityDuaration(card.WEDuration)
            customCard.setActivityEndDate(card.WEDateEnded)

            if (card.getSavedImage() != null)
            {
                Log.w("log", "there be an image")
                customCard.SetImage(card.getSavedImage()!!)
                Log.w("log", "added image")
            }

            var imgActivity = customCard.findViewById<ImageView>(R.id.imgActivity)
            imgActivity.setOnClickListener {
                enlargeImage(imgActivity)
            }

            linView.addView(customCard)
            Log.w("log", "card added ")
        }
    }

    private fun enlargeImage(imageView: ImageView) {
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