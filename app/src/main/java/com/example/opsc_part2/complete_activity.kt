package com.example.opsc_part2

import Classes.ProfileImageManager
import Classes.ToolBox
import Classes.WorkEntriesObject
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

private const val paraActivityIDIn = -1

class complete_activity : BottomSheetDialogFragment() {
    private var paraActivityID: Int? = null
    private var paraTime: String? = null
    private lateinit var btnAddImage: Button
    private lateinit var btnSave: Button
    private var image: Bitmap? = null

    // private lateinit var newWorkEntriesObject: WorkEntriesObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paraActivityID = it.getInt(paraActivityIDIn.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val View = inflater.inflate(R.layout.fragment_complete_activity, container, false)


        btnSave = View.findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener()
        {
            AddEntry()
        }

        btnAddImage = View.findViewById<Button>(R.id.btnAddImage)
        btnAddImage.setOnClickListener()
        {
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

        return View
    }

    //============================================================================
    //call the camera
    private fun startCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, ProfileFragment.CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT).show()
        }
    }

    //============================================================================
    //when camera is done get the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            image = imageBitmap
        }
    }

    //============================================================================
    //add data to object array
    private fun AddEntry() {
        val newWorkEntriesObject =
            WorkEntriesObject(paraActivityID!!, ToolBox.ActiveUserID, 3, "time")

        image?.let { bitmap ->
            if (bitmap.isMutable) {
                newWorkEntriesObject.saveImage(bitmap)
            }
        }

        ToolBox.WorkEntriesList.add(newWorkEntriesObject)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            complete_activity().apply {
                arguments = Bundle().apply {
                    putInt(paraActivityID.toString(), param1)
                }
            }

        private const val CAMERA_PERMISSION_CODE = 100
        private const val CAMERA_REQUEST_CODE = 200
    }
}