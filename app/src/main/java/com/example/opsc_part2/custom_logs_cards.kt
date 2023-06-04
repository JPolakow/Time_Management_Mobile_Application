package com.example.opsc_part2

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class custom_logs_cards @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0

) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_logs_cards, this, true)

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
    fun setActivityDuaration(Duration: String) {
        val txtDuration = findViewById<TextView>(R.id.txtDuration)
        txtDuration.text = Duration;
    }

    //============================================================================
    // Used to set end date
    fun setActivityEndDate(EndDate: String) {
        val tvDateEnded = findViewById<TextView>(R.id.tvDateEnded)
        tvDateEnded.text = EndDate;
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
