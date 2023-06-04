package com.example.opsc_part2

import Classes.ProfileImageManager
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var imageView: ImageView

    companion object {
        internal const val CAMERA_PERMISSION_CODE = 100
        internal const val CAMERA_REQUEST_CODE = 200
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        // ------------ SIGN OUT CLICK ------------ //
        // Add functionality for clearing user data
        val signOutClick = view.findViewById<Button>(R.id.btnLogout)
        signOutClick.setOnClickListener {

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

        // ------------ CAMERA ------------ //
        imageView = view.findViewById(R.id.imageView)
        val captureButton: Button = view.findViewById(R.id.captureButton)
        captureButton.setOnClickListener {
            //see if app has permission to use camera
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
        // ------------ END OF CAMERA ------------ //

        return view
    }

    //============================================================================
    // Load the image if it exists in the ImageManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ProfileImageManager.loadImage()?.let { image ->
            imageView.setImageBitmap(image)
        }
    }

    //============================================================================
    //call the camera
    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT).show()
        }
    }

    //============================================================================
    //when camera is done get the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageView.setImageBitmap(imageBitmap)

            imageBitmap?.let {
                //saveImageLocally(it)
                ProfileImageManager.saveImage(it)
            }
        }
    }
}