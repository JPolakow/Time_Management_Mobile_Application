package Classes

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class RetreiveData {

    companion object {

        // =========================================================================================//
        // Function to load user categories into object list from Firebase
        fun LoadUserCategories(userID: String, callback: (String) -> Unit) {
            val listIn = mutableListOf<CategoryObject>()
            val db = Firebase.firestore

            db.collection("categories").whereEqualTo("CategoryUserID", userID).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        listIn.add(
                            CategoryObject(
                                document.getString("CategoryName").toString(),
                                document.getString("CategoryUserID").toString()
                            )
                        )
                    }
                    ToolBox.CategoryList = listIn
                    callback("success")
                }.addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    callback("failure")
                }
        }

        // =========================================================================================//
        // Function to load user profile details into object list from Firebase
        fun LoadUserProfile(userID: String, callback: (String) -> Unit) {
            val userList = mutableListOf<UserClass>()
            val db = Firebase.firestore

            db.collection("users").document(userID).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = UserClass(
                            document.id,
                            document.getString("username").toString(),
                            document.getString("name").toString(),
                            document.getString("surname").toString()
                        )
                        userList.add(user)
                        ToolBox.UsersList = userList
                        callback("success")
                    } else {
                        callback("failure")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, exception.toString())
                    callback("failure")
                }
        }

        // =========================================================================================//
        // Function to load user Activities into object list from Firebase
        fun LoadActivities(userID: String, callback: (String) -> Unit) {
            val listIn = mutableListOf<ActivityObject>()
            val db = Firebase.firestore

            db.collection("activities").whereEqualTo("ActivityUserID", userID).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        listIn.add(
                            ActivityObject(
                                document.id,
                                document.getString("ActivityUserID").toString(),
                                document.getString("ActivityName").toString(),
                                document.getString("ActivityCategory").toString(),
                                document.getString("DateCreated").toString(),
                                document.getDouble("ActivityMinGoal")!!,
                                document.getDouble("ActivityMaxGoal")!!,
                                document.getString("ActivityColor").toString(),
                                document.getString("ActivityDescription").toString()
                            )
                        )
                    }
                    ToolBox.ActivitiesList = listIn
                    callback("success")
                }.addOnFailureListener { exception ->
                    Log.d(
                        ContentValues.TAG, exception.toString()
                    )
                    callback("failure")
                }
        }

        // =========================================================================================//
        // Function to load user Work Entries details into object list from Firebase
        fun LoadWorkEntries(userID: String, callback: (String) -> Unit) {
            val listIn = mutableListOf<WorkEntriesObject>()
            val db = Firebase.firestore

            db.collection("workEntries").whereEqualTo("WEUserID", userID).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        listIn.add(
                            WorkEntriesObject(
                                document.id,
                                document.getString("WEActivityID").toString(),
                                document.getString("WEActivityName").toString(),
                                document.getString("WEActivityCategory").toString(),
                                document.getString("WEUserID").toString(),
                                document.getDouble("WERating")!!.toInt(),
                                document.getString("WEDateEnded").toString(),
                                document.getDouble("WEDuration")!!,
                                document.getString("WEColor").toString()
                            )
                        )
                    }
                    ToolBox.WorkEntriesList = listIn
                    callback("success")
                }.addOnFailureListener { exception ->
                    Log.d(
                        ContentValues.TAG, exception.toString()
                    )
                    callback("failure")
                }
        }

        // =========================================================================================//
        // Function to load user Images into object list from Firebase
        fun loadImages(userID: String, callback: (String) -> Unit) {
            val db = Firebase.firestore

            db.collection("images").whereEqualTo("userId", userID).get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        val workEntryID = document.getString("WorkEntryID") ?: continue
                        val index = ToolBox.WorkEntriesList.indexOfFirst { we ->
                            we.WEID == workEntryID
                        }

                        if (index != -1) {

                            val base64Image = document.getString("imageUrl")

                            // Decoding the base64-encoded string back into a byte array
                            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)

                            // Converting the byte array to a Bitmap object
                            val bitmap =
                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                            ToolBox.WorkEntriesList[index].saveImage(bitmap)
                        }
                    }
                    callback("success")
                }.addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "Failure occurred", exception)
                    callback("failure")
                }
        }
    }
}