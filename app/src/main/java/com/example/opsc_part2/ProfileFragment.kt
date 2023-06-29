package com.example.opsc_part2

import Classes.ToolBox
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var username: TextView

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)
        try {
            name = view.findViewById(R.id.tvDisplayName)
            surname = view.findViewById(R.id.tvDisplaySurname)
            username = view.findViewById(R.id.tvDisplayUsername)

            val userIndex = ToolBox.UsersList.indexOfFirst { user -> user.UserKey == ToolBox.ActiveUserID }

            name.setText(ToolBox.UsersList[userIndex].UserName)
            surname.setText(ToolBox.UsersList[userIndex].UserSurname)
            username.setText(ToolBox.UsersList[userIndex].UserUsername)

            // ------------ SIGN OUT CLICK ------------ //
            // Add functionality for clearing user data
            val signOutClick = view.findViewById<ImageButton>(R.id.btnLogout)
            signOutClick.setOnClickListener {

                animateButtonClick(signOutClick)

                //Sign out
                // Creating a new Dialog
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        // When User selects "Yes"
                        DialogInterface.BUTTON_POSITIVE -> {
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        }
                        // When User selects "No"
                        DialogInterface.BUTTON_NEGATIVE -> {
                            // No button clicked
                            // DO nothing
                        }
                    }
                }

                // Building a new alert
                val builder = AlertDialog.Builder(context)
                // Setting alert message
                builder.setMessage("Are you sure you want to logout?")
                    // Setting text for positive button
                    .setPositiveButton("Yes", dialogClickListener)
                    // Setting text for negative button
                    .setNegativeButton("No", dialogClickListener)
                    // Showing dialog
                    .show()
            }
            // ------------ END SIGN OUT CLICK ------------ //

        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }

        return view
    }

    //============================================================================
    // Suppose to be a method to animate the logout click in profile
    private fun animateButtonClick(imageButton: ImageButton) {
        try {
            val colorMatrix = ColorMatrix().apply {
                setSaturation(0f) // Set saturation to 0 to convert to grayscale
            }
            val colorFilter = ColorMatrixColorFilter(colorMatrix)
            imageButton.colorFilter = colorFilter

            val animator = ObjectAnimator.ofFloat(imageButton, "alpha", 1f, 0.5f)
            animator.duration = 200
            animator.repeatMode = ValueAnimator.REVERSE
            animator.repeatCount = 1

            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    imageButton.clearColorFilter()
                }
            })

            animator.start()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }
}