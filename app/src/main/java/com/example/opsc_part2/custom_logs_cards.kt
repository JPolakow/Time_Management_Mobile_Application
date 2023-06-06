package com.example.opsc_part2

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.w3c.dom.Text

class custom_logs_cards @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_logs_cards, this, true)

        // Perform any initialization or customization here
        // You can access and modify the views within the custom component layout
    }

    //============================================================================
    // Used to set activity Name
    fun setActivityName(name: String) {
        val activityName = findViewById<TextView>(R.id.tvActivityName)
        activityName.text = name;
    }

    //============================================================================
    // Used to set image
    fun SetImage(image: Bitmap) {
        val imgActivity = findViewById<ImageView>(R.id.imgActivity)
        imgActivity.setImageBitmap(image)
    }

    //============================================================================
    // Used to set duration
    fun setActivityDuaration(Duration: Double) {
        val txtDuration = findViewById<TextView>(R.id.txtDuration)
        txtDuration.text = Duration.toString();
    }

    //============================================================================
    // Used to set end date
    fun setActivityEndDate(EndDate: String) {
        val tvDateEnded = findViewById<TextView>(R.id.tvDateEnded)
        tvDateEnded.text = EndDate;
    }

    //============================================================================
    // Used to set the rating user has selected
    fun setRating(ratingInt: Int) {
        val tvRating = findViewById<TextView>(R.id.tvRating)

        tvRating.text = ratingInt.toString()
    }

    //============================================================================
    // Uses to set rating color
    fun setRatingColor(ratingNumber: Int) {
        val tvRating = findViewById<TextView>(R.id.tvRating)

        val colorResource = when (ratingNumber) {
            1 -> R.color.Red
            2 -> R.color.Orange
            3 -> R.color.Yellow
            4 -> R.color.Green
            5 -> R.color.Blue
            else -> R.color.Blue // Replace with your default color resource ID
        }
        val colorToSet = ContextCompat.getColorStateList(context, colorResource)
        tvRating.setTextColor(colorToSet)
    }

    //============================================================================
    // Used to set the background color of card
    fun setCardColor(selectedColor: String) {
        val colorResource = when (selectedColor) {
            "Red" -> R.color.redPastel
            "Blue" -> R.color.bluePastel
            "Purple" -> R.color.purplePastel
            "Pink" -> R.color.pinkPastel
            "Light-Blue" -> R.color.lightBluePastel
            else -> R.color.Blue // Replace with your default color resource ID
        }
        val colorToSet = ContextCompat.getColorStateList(context, colorResource)
        val cardLayout = findViewById<androidx.cardview.widget.CardView>(R.id.cardView)
        cardLayout.backgroundTintList = colorToSet
    }
}
