package com.example.opsc_part2

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    ////-=-=-=-=-CAMERA-=-=-=-=-
    private lateinit var imageView: ImageView
    private lateinit var imageViewModel: ImageViewModel

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val CAMERA_REQUEST_CODE = 200
    }
    //-=-=-=-=-END OF CAMERA-=-=-=-=-

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_settings, container, false)

        imageView = view.findViewById(R.id.imageView)

        //-=-=-=-=-CAMERA-=-=-=-=-

        // Binding val signOutClick to UI Element btnLogout
        val signOutClick = view.findViewById<Button>(R.id.btnLogout)

        val test = view.findViewById<TextView>(R.id.tvDisplayName)
        val captureButton: Button = view.findViewById(R.id.captureButton)
        captureButton.setOnClickListener {
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

        // Initialize the ViewModel
        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        imageViewModel.getSavedImage()?.let { image ->
            imageView.setImageBitmap(image)
        }
        //-=-=-=-=-END OF CAMERA-=-=-=-=-

        // ------------ SIGN OUT CLICK ------------ //
        // Add functionality for clearing user data
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
            // ------------ END SIGN OUT CLICK ------------ //
        }

        return view
    }

    //============================================================================
    //============================================================================
    //============================================================================

    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageView.setImageBitmap(imageBitmap)

            // Save the image locally and update ViewModel
            imageBitmap?.let {
                saveImageLocally(it)
                imageViewModel.saveImage(it)
            }
        }
    }

    private fun saveImageLocally(imageBitmap: Bitmap) {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_$timeStamp.jpg"

        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, imageFileName)

        try {
            val fileOutputStream = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()

            Toast.makeText(requireContext(), "Image saved successfully", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}

class ImageViewModel : ViewModel() {
    private var savedImage: Bitmap? = null

    fun saveImage(image: Bitmap) {
        savedImage = image
    }

    fun getSavedImage(): Bitmap? {
        return savedImage
    }
}
