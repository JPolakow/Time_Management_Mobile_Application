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
import android.util.Log
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
private const val paraDurationIn = ""
private const val paraColorIn = ""

class complete_activity : BottomSheetDialogFragment() {
    companion object {
        internal const val CAMERA_PERMISSION_CODE = 100
        internal const val CAMERA_REQUEST_CODE = 200
    }

    //arguments inputs
    private var paraActivityID: Int? = null
    private var paraDuration: Double? = null
    private var paraColor: String? = null
    private var paraName: String? = null
    private var paraCatagory: String? = null

    //UI
    private lateinit var btnAddImage: Button
    private lateinit var btnSave: Button
    private var image: Bitmap? = null
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnFour: Button
    private lateinit var btnFive: Button

    //regular
    private var rating: Int = 1

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_complete_activity, container, false)

        try {
            paraColor = arguments?.getString("color")
            paraDuration = arguments?.getDouble("duration")
            paraActivityID = arguments?.getInt("id")
            paraName = arguments?.getString("name")
            paraCatagory = arguments?.getString("category")

            btnSave = view.findViewById<Button>(R.id.btnSave)
            btnSave.setOnClickListener {
                AddEntry()
            }

            btnAddImage = view.findViewById<Button>(R.id.btnAddImage)
            btnAddImage.setOnClickListener {
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
                        complete_activity.CAMERA_PERMISSION_CODE
                    )
                }

                btnOne = view.findViewById(R.id.btnOne)
                btnOne.setOnClickListener(){rating = 1}

                btnTwo = view.findViewById(R.id.btnTwo)
                btnTwo.setOnClickListener(){rating = 2}

                btnThree = view.findViewById(R.id.btnThree)
                btnThree.setOnClickListener(){rating = 3}

                btnFour = view.findViewById(R.id.btnFour)
                btnFour.setOnClickListener(){rating = 4}

                btnFive = view.findViewById(R.id.btnFive)
                btnFive.setOnClickListener(){rating = 5}
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //============================================================================
    // call the camera
    private fun startCamera() {
        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(cameraIntent, complete_activity.CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(requireContext(), "Camera is not available", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // when the camera is done get the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == complete_activity.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (data != null && data.extras != null && data.extras!!.containsKey("data")) {
                    val imageBitmap = data.extras!!.get("data") as Bitmap?
                    if (imageBitmap != null) {
                        Log.d("log", "Image captured successfully")
                        image = imageBitmap
                        // Now you can call the saveImage() method if necessary
                    } else {
                        Log.e("log", "Failed to retrieve the image bitmap from the camera")
                    }
                } else {
                    Log.e("log", "No image data received from the camera")
                }
            } else {
                Log.e("log", "Camera capture was unsuccessful")
            }
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // add data to object array
    private fun AddEntry() {
        try {
            // Creating correct date format
            val time = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())

            val newWorkEntriesObject =
                WorkEntriesObject(
                    paraActivityID!!,
                    paraName!!,
                    paraCatagory!!,
                    ToolBox.ActiveUserID,
                    rating,
                    time,
                    paraDuration!!,
                    paraColor!!
                )

            Log.w("log", "image start")
            image?.let { bitmap ->
                newWorkEntriesObject.saveImage(bitmap)
                Log.w("log", "IMAGED SAVED")
            }

            ToolBox.WorkEntriesList.add(newWorkEntriesObject)
            dismiss()
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }
}
