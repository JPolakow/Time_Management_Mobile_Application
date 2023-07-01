package Classes

import java.util.*

// Object for Users' activities
class ActivityObject(
    var ActivityID: String,
    val ActivityUserID: String,
    val ActivityName: String,
    val ActivityCategory: String,
    val DateCreated: String,
    val ActivityMinGoal: Double,
    val ActivityMaxGoal: Double,
    val ActivityColor: String,
    val ActivityDescription: String,
    ) {
}