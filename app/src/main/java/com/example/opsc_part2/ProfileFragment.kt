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

    // Initializing variables
    private lateinit var name: TextView
    private lateinit var surname: TextView
    private lateinit var username: TextView
    private lateinit var signOut: ImageButton

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)
        try {

            // Binding views
            name = view.findViewById(R.id.tvDisplayName)
            surname = view.findViewById(R.id.tvDisplaySurname)
            username = view.findViewById(R.id.tvDisplayUsername)
            signOut = view.findViewById(R.id.btnLogout)

            val userIndex =
                ToolBox.UsersList.indexOfFirst { user -> user.UserKey == ToolBox.ActiveUserID }

            if (userIndex != -1) {
                name.text = ToolBox.UsersList[userIndex].UserName
                surname.text = ToolBox.UsersList[userIndex].UserSurname
                username.text = ToolBox.UsersList[userIndex].UserUsername
            }

            // ------------ SIGN OUT CLICK ------------ //
            signOut.setOnClickListener {
                var a = 0
                animateButtonClick(signOut)
                // Sign out
                //       Creating a new Dialog
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
                // Set saturation to 0 to convert to grayscale
                setSaturation(0f)
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