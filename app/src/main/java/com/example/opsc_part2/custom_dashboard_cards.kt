package com.example.opsc_part2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class custom_dashboard_cards @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.custom_dashboard_cards, this, true)

        // Perform any initialization or customization here
        // You can access and modify the views within the custom component layout
    }

    // These methods are called within the dahsboard activity

    //============================================================================
    // Used to set activity Name
    fun setActivityName(name: String) {
        // Update the activity name view
        val activityName = findViewById<TextView>(R.id.tvActivityName)

        activityName.text = name;
    }

    //============================================================================
    // Used to set activity start Date
    fun setActivityStartDate(startDate: String) {

        val actStartDate = findViewById<TextView>(R.id.tvDateCreated)

        actStartDate.text = startDate;
    }
    //============================================================================

    fun setActivityMinGoal(minGoal: String)
    {

        val minGoalDisplay = findViewById<TextView>(R.id.tvMinGoal)

        minGoalDisplay.text =   minGoal

    }
    //============================================================================
    fun setActivityMaxGoal(maxGoal: String)
    {

        val maxGoalDisplay = findViewById<TextView>(R.id.tvMaxGoal)

        maxGoalDisplay.text = maxGoal

    }

    //============================================================================
    // Used to set the background color of card
    fun setCardColor(selectedColor: String) {
        val colorResource = when (selectedColor) {
            "Red" -> R.color.Red
            "Blue" -> R.color.Blue
            "Green" -> R.color.Green
            "Yellow" -> R.color.Yellow
            "Cyan" -> R.color.Cyan
            "Magenta" -> R.color.Magenta
            "Orange" -> R.color.Orange
            "Purple" -> R.color.Purple
            "Lime" -> R.color.Lime
            "Teal" -> R.color.Teal
            "Olive" -> R.color.Olive
            "Maroon" -> R.color.Maroon
            "Navy" -> R.color.Navy
            "Pink" -> R.color.Pink
            else -> R.color.Blue // Replace with your default color resource ID
        }
        val colorToSet = ContextCompat.getColorStateList(context, colorResource)
        val cardLayout = findViewById<RelativeLayout>(R.id.relCard)
        cardLayout.backgroundTintList = colorToSet
    }
}
