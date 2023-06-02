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

        val signOutClick = view.findViewById<Button>(R.id.btnLogout)


        // ------------ SIGN OUT CLICK ------------ //
        // Add functionality for clearing user data

        signOutClick.setOnClickListener{

            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        // No button clicked
                        // DO nothing
                    }
                }
            }

            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to logout?\nYou will lose all progress!")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show()


        }

        // ------------ END SIGN OUT CLICK ------------ //


        return view
    }
}
