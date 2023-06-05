package Classes

import java.text.SimpleDateFormat
import java.util.*

class ToolBox {
    companion object {
        var ActiveUserID: Int = -1

        //used to move data between addGoal and addActivity
        var MinGoal: Int = -1
        var MaxGoal: Int = -1

        //object arrays
        val WorkEntriesList = mutableListOf<WorkEntriesObject>(
            WorkEntriesObject(1, "Name",1, 5, "1/1/12", "4", "Red")
        )

        val CatagoryList = mutableListOf<CatagoryObject>(
            CatagoryObject("Collage", 1)
        )

        val UsersList = mutableListOf<ActiveUserClass>(
            //default user, password = pass
            ActiveUserClass(
                "Name",
                "Surname",
                "user",
                "D74FF0EE8DA3B9806B18C877DBF29BBDE50B5BD8E4DAD7A3A725000FEB82E8F1"
            )
        )

        val ActivitiesList = mutableListOf<ActivityObject>(
            ActivityObject(
                0,
                0,
                "Open-Source",
                "Collage",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                2,
                4,
                "Light-Blue"
            ),
            ActivityObject(
                1,
                0,
                "Programming",
                "Collage",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                6,
                8,
                "Red"
            ),
            ActivityObject(
                2,
                0,
                "Research",
                "Collage",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                1,
                3,
                "Pink"
            ),
            ActivityObject(
                3,
                0,
                "Project Management",
                "Collage",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                3,
                7,
                "Blue"
            )
        )
    }
}