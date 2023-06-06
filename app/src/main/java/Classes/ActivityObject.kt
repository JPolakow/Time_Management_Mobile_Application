package Classes

import java.util.*

class ActivityObject(
    val ActivityID: Int,
    val ActivityUserID: Int,
    val ActivityName: String,
    val ActivityCategory: String,
    val DateCreated: String,
    val ActivityMinGoal: Double,
    val ActivityMaxGoal: Double,
    //val ActivityIcon: String
    val ActivityColor: String,
    var timer: Timer? = null,
    ) {
}