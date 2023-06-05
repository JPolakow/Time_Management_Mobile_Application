package com.example.opsc_part2

import Classes.ToolBox
import Classes.WorkEntriesObject
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val paraActivityIDIn = -1
private const val paraDurationIn = ""
private const val paraColorIn = ""

class complete_activity : BottomSheetDialogFragment() {
    private var paraActivityID: Int? = null
    private var paraDuration: String? = null
    private var paraColor: String? = null
    private var paraName: String? = null

    private lateinit var btnAddImage: Button
    private lateinit var btnSave: Button
    private var image: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_complete_activity, container, false)

        paraColor = arguments?.getString("color")
        paraDuration = arguments?.getString("duration")
        paraActivityID = arguments?.getInt("id")
        paraName = arguments?.getString("name")

        btnSave = view.findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            AddEntry()
        }

        btnAddImage = view.findViewById<Button>(R.id.btnAddImage)
        btnAddImage.setOnClickListener {
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
                    ProfileFragment.CAMERA_PERMISSION_CODE
                )
            }
        }

        return view
    }

    //============================================================================
    // call the camera
    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, ProfileFragment.CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT).show()
        }
    }

    //============================================================================
    // when the camera is done get the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            image = imageBitmap
        }
    }

    //============================================================================
    // add data to object array
    private fun AddEntry() {
        try {
            val newWorkEntriesObject =
                WorkEntriesObject(
                    paraActivityID!!,
                    paraName!!,
                    ToolBox.ActiveUserID,
                    3,
                    "time",
                    paraDuration!!,
                    paraColor!!
                )

            image?.let { bitmap ->
                if (bitmap.isMutable) {
                    newWorkEntriesObject.saveImage(bitmap)
                }
            }

            ToolBox.WorkEntriesList.add(newWorkEntriesObject)
            dismiss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    //============================================================================
    // constructor
    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val CAMERA_REQUEST_CODE = 200
    }
}
