package Classes

import java.util.*

class ActivityObject(
    val ActivityID: Int,
    val ActivityUserID: Int,
    val ActivityName: String,
    val ActivityCategory: String,
    val DateCreated: String,
    val ActivityMinGoal: Int,
    val ActivityMaxGoal: Int,
    //val ActivityIcon: String
    val ActivityColor: String,
    var timer: Timer? = null,
    ) {
}