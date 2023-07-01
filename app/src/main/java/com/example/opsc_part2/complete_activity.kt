package com.example.opsc_part2

import Classes.ActivityObject
import Classes.ToolBox
import androidx.lifecycle.viewModelScope
import Classes.WorkEntriesObject
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
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
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class complete_activity : BottomSheetDialogFragment() {
    companion object {
        internal const val CAMERA_PERMISSION_CODE = 100
        internal const val CAMERA_REQUEST_CODE = 200
    }

    //arguments inputs
    private var paraActivityID: String? = null
    private var paraDuration: Double? = null
    private var paraColor: String? = null
    private var paraName: String? = null
    private var paraCategory: String? = null

    //UI
    private lateinit var btnAddImage: Button
    private lateinit var btnSave: Button
    private var image: Bitmap? = null
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnFour: Button
    private lateinit var btnFive: Button

    //Regular
    private var rating: Int = 1
    private var Key: String = ""

    private var callback: CompleteActivityCallback? = null

    //============================================================================
    interface CompleteActivityCallback {
        fun onActivityComplete()
    }

    //============================================================================
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CompleteActivityCallback) {
            callback = context
        }
    }

    //============================================================================
    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    //============================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_complete_activity, container, false)

        try {
            //retreive data send from the dashboard, loadCustomUI method
            paraColor = arguments?.getString("color")
            paraDuration = arguments?.getDouble("duration")
            paraActivityID = arguments?.getString("id")
            paraName = arguments?.getString("name")
            paraCategory = arguments?.getString("category")

            //save
            btnSave = view.findViewById<Button>(R.id.btnSave)
            btnSave.setOnClickListener {
                AddEntry()
            }

            //add image
            btnAddImage = view.findViewById<Button>(R.id.btnAddImage)
            btnAddImage.setOnClickListener {
                //Ensure app has permission to use camera
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.CAMERA
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
            }

            // Call the method to fetch work entries from Firestore
            GlobalScope.launch {
                try {
                    fetchWorkEntriesFromFirestore()
                    // Handle the retrieved work entries and their associated images
                } catch (exception: Exception) {
                    // Handle the failure
                }
            }

            //ratings
            btnOne = view.findViewById(R.id.btnOne)
            btnOne.setOnClickListener() { rating = 1 }

            btnTwo = view.findViewById(R.id.btnTwo)
            btnTwo.setOnClickListener() { rating = 2 }

            btnThree = view.findViewById(R.id.btnThree)
            btnThree.setOnClickListener() { rating = 3 }

            btnFour = view.findViewById(R.id.btnFour)
            btnFour.setOnClickListener() { rating = 4 }

            btnFive = view.findViewById(R.id.btnFive)
            btnFive.setOnClickListener() { rating = 5 }

        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
        return view
    }

    //============================================================================
    // Call the camera
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
    // When the camera is done get the image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == complete_activity.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (data != null && data.extras != null && data.extras!!.containsKey("data")) {
                    val imageBitmap = data.extras!!.get("data") as Bitmap?
                    if (imageBitmap != null) {
                        Log.d("log", "Image captured successfully")
                        image = imageBitmap

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
    // Add data to object array
    private fun AddEntry() {
        // Creating an instance of firebase storage
        val storage = Firebase.storage
        // Create a storage reference
        var storageRef = Firebase.storage.reference

        try {
            // Creating correct date format
            val time = SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(Date())
            val duration = String.format("%.2f", paraDuration!! / 60).toDouble()

            var newWorkEntriesObject = WorkEntriesObject(
                "",
                paraActivityID!!,
                paraName!!,
                paraCategory!!,
                ToolBox.ActiveUserID,
                rating,
                time,
                duration,
                paraColor!!
            )

            image?.let { bitmap ->
                newWorkEntriesObject.saveImage(bitmap)
            }

            //writeToDB callback
            writeToDB(newWorkEntriesObject) { outcome ->
                if (outcome) {
                    newWorkEntriesObject.WEID = Key
                    ToolBox.WorkEntriesList.add(newWorkEntriesObject)
                    uploadImageToFirebaseStorage(image!!)
                } else {
                    // Failure
                }
            }

            callback?.onActivityComplete()
            dismiss()
        } catch (ex: Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    //save new work entry to db
    private fun writeToDB(newWorkEntry: WorkEntriesObject, callback: (Boolean) -> Unit) {
        val db = Firebase.firestore

        val newActivity = hashMapOf(
            "WEID" to newWorkEntry.WEID,
            "WEActivityID" to newWorkEntry.WEActivityID,
            "WEActivityName" to newWorkEntry.WEActivityName,
            "WEActivityCategory" to newWorkEntry.WEActivityCategory,
            "WEUserID" to newWorkEntry.WEUserID,
            "WERating" to newWorkEntry.WERating,
            "WEDateEnded" to newWorkEntry.WEDateEnded,
            "WEDuration" to newWorkEntry.WEDuration,
            "WEColor" to newWorkEntry.WEColor
        )

        db.collection("workEntries")
            .add(newActivity)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Key = documentReference.id
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }
    private fun uploadImageToFirebaseStorage(imageBitmap: Bitmap) {
        // Get a reference to the Firebase Storage root
        val storageRef = Firebase.storage.reference

        // Path where image is being stored
        val userId = ToolBox.ActiveUserID

        val imagePath = "images/$userId/$Key.jpg"

        // Create a reference to the image file in Firebase Storage
        val imageRef = storageRef.child(imagePath)

        // Convert the Bitmap to a byte array
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        // Upload the image data to Firebase Storage
        val uploadTask = imageRef.putBytes(imageData)

        // Add a listener to track the upload progress or handle success/failure
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image upload successful
            // You can retrieve the image URL using taskSnapshot.metadata?.reference?.downloadUrl
            val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl

            // Save the image URL and other details in Firestore
            saveImageDetailsToFirestore(imageUrl.toString(), userId, Key)
        }.addOnFailureListener { exception ->
            // Image upload failed
            // Handle the failure and show an error message to the user
        }
    }
    private fun saveImageDetailsToFirestore(imageUrl: String, userId: String, WorkEntryID: String) {
        val db = Firebase.firestore
        val imageDetails = hashMapOf(
            "imageUrl" to imageUrl,
            "userId" to userId,
            "WorkEntryID" to WorkEntryID
            // Add any other relevant details
        )

        db.collection("images").add(imageDetails)
            .addOnSuccessListener { documentReference ->
                Log.d("Image Success","Uploaded Image Successfully")
                // You can access the document ID using documentReference.id if needed
            }
            .addOnFailureListener { exception ->
                // Image details save failed
                // Handle the failure and show an error message to the user
            }
    }


    private suspend fun getImageUrlForWorkEntry(workEntryID: String): String? {
        val db = Firebase.firestore
        val querySnapshot = db.collection("images")
            .whereEqualTo("WorkEntryID", workEntryID)
            .get()
            .await() // Use 'await()' to suspend the coroutine and wait for the result

        return if (!querySnapshot.isEmpty) {
            val documentSnapshot = querySnapshot.documents[0]
            val imageUrl = documentSnapshot.getString("imageUrl")
            imageUrl
        } else {
            null
        }
    }

    private suspend fun fetchWorkEntriesFromFirestore() {
        val db = Firebase.firestore
        val workEntriesCollection = db.collection("workEntries")

        try {
            val querySnapshot = workEntriesCollection.get().await()
            val workEntriesList = mutableListOf<WorkEntriesObject>()

            for (document in querySnapshot) {
                val workEntry = document.toObject(WorkEntriesObject::class.java)
                workEntry.setImageUrl(getImageUrlForWorkEntry(Key)!!)
                workEntriesList.add(workEntry)
            }

            // Handle the retrieved work entries list
            // Display the work entries and their associated images
        } catch (exception: Exception) {
            // Handle the failure
        }
    }






}
