package Classes

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ToolBox {
    companion object {
        var ActiveUserID: Int = -1

        //used to move data between addgoal and addactivity
        var MinGoal: Int = -1
        var MaxGoal: Int = -1

        //object arrays
        val WorkEntriesList = mutableListOf<WorkEntriesObject>()
        val UsersList = mutableListOf<ActiveUserClass>(
            //default user, password = pass
            ActiveUserClass(
                "Name",
                "Surname",
                "user",
                "D74FF0EE8DA3B9806B18C877DBF29BBDE50B5BD8E4DAD7A3A725000FEB82E8F1"
            )
        )

        @RequiresApi(Build.VERSION_CODES.O)
        val ActivitiesList = mutableListOf<ActivityObject>(
            ActivityObject(
                1,
                1,
                "Open-Source",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                2,
                4,
                "Yellow"
            ),
            ActivityObject(
                2,
                2,
                "Programming",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                6,
                8,
                "Cyan"
            ),
            ActivityObject(
                3,
                3,
                "Research",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                1,
                3,
                "Lime"
            ),
            ActivityObject(
                4,
                4,
                "Project Management",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                3,
                7,
                "Magenta"
            )
        )
    }
}