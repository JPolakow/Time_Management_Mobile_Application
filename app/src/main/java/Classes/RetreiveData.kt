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
        fun LoadUserCategories(userID: String, callback: (String) -> Unit) {
            val listIn = mutableListOf<CategoryObject>()
            val db = Firebase.firestore

            db.collection("categories")
                .whereEqualTo("CategoryUserID", userID)
                .get()
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
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    callback("failure")
                }
        }


        fun LoadActivities(userID: String, callback: (String) -> Unit) {
            val listIn = mutableListOf<ActivityObject>()
            val db = Firebase.firestore

            db.collection("activities")
                .whereEqualTo("ActivityUserID", userID)
                .get()
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
                }
                .addOnFailureListener { exception ->
                    Log.d(
                        ContentValues.TAG,
                        exception.toString()
                    )
                    callback("failure")
                }
        }

        fun LoadWorkEntries(userID: String, callback: (String) -> Unit) {
            val listIn = mutableListOf<WorkEntriesObject>()
            val db = Firebase.firestore

            db.collection("workEntries")
                .whereEqualTo("WEUserID", userID)
                .get()
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
                }
                .addOnFailureListener { exception ->
                    Log.d(
                        ContentValues.TAG,
                        exception.toString()
                    )
                    callback("failure")
                }
        }

        fun loadImages(userID: String, callback: (String) -> Unit) {
            val db = Firebase.firestore

            db.collection("images")
                .whereEqualTo("userId", userID)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {

                        val workEntryID = document.getString("WorkEntryID") ?: continue
                        val index = ToolBox.WorkEntriesList.indexOfFirst { we ->
                            we.WEID == workEntryID
                        }

                        if (index != -1) {

                            // Assuming you have retrieved the base64Image string from Firestore
                            val base64Image = document.getString("imageUrl")

                            // Decode the base64-encoded string back into a byte array
                            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)

                            // Convert the byte array to a Bitmap object
                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                            ToolBox.WorkEntriesList[index].saveImage(bitmap)

var a = 0
                        }
                    }
                    callback("success")
                }
                .addOnFailureListener { exception ->
                    Log.e(ContentValues.TAG, "Failure occurred", exception)
                    callback("failure")
                    // You can throw the exception here if needed
                }
        }


        fun getImageFromFirebaseStorage(
            path: String,
            onSuccess: (Bitmap) -> Unit,
            onFailure: (Exception) -> Unit
        ) {

        }
    }
}