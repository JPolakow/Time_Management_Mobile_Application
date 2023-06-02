package com.example.opsc_part2

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        // Binding val signOutClick to UI Element btnLogout
        val signOutClick = view.findViewById<Button>(R.id.btnLogout)


        // ------------ SIGN OUT CLICK ------------ //
        // Add functionality for clearing user data
        signOutClick.setOnClickListener{

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
            builder.setMessage("Are you sure you want to logout?\nYou will lose all progress!")
                    // Setting text for positive button
                .setPositiveButton("Yes", dialogClickListener)
                    // Setting text for negative button
                .setNegativeButton("No", dialogClickListener)
                    // Showing dialog
                .show()


        }

        // ------------ END SIGN OUT CLICK ------------ //


        return view
    }
}
