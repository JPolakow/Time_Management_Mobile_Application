package Classes

import com.example.opsc_part2.R
import java.time.LocalDate
import java.util.*

class ActivityObject(
    val ActivityID: Int,
    val ActivityUserID: Int,
    val ActivityName: String,
    // val ActivityCatagory: String,
    val DateCreated: String,
    val ActivityMinGoal: String,
    val ActivityMaxGoal: String,
    //val ActivityIcon: String
    val ActivityColor: String,
    //val ActivityDescription: String,
    var timer: Timer? = null,
    ) {
}