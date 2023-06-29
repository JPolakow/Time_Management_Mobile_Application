package Classes

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
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
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                    callback("failure")
                }
        }
    }

    private fun writeToDB() {
        val db = Firebase.firestore

        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    //============================================================================
    private fun readFromDB() {
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

}