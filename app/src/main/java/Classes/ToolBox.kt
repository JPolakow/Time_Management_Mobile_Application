package Classes

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class ToolBox {
    companion object {
        var ActiveUserID: Int = -1

        //used to store selected category
        var SelectedCategory: String = "None"

        //object arrays
        val WorkEntriesList = mutableListOf<WorkEntriesObject>(
            WorkEntriesObject(1, "Name1", "College", 1, 5, "06-06-2023", "04:00", "Red"),
            WorkEntriesObject(1, "Name2", "College", 1, 5, "07-06-2023", "09:10", "Red"),
            WorkEntriesObject(1, "Name3", "College", 1, 5, "08-06-2023", "11:08", "Red"),
            WorkEntriesObject(1, "Name4", "Test", 1, 5, "09-06-2023", "01:01", "Red"),
            WorkEntriesObject(1, "Name5", "Test", 2, 5, "10-06-2023", "00:10", "Red")
        )

        val CategoryList = mutableListOf<CategoryObject>(
            CategoryObject("College", 1)
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

        @SuppressLint("ConstantLocale")
        val ActivitiesList = mutableListOf<ActivityObject>(
            ActivityObject(
                0,
                0,
                "Open-Source",
                "College",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                60.0,
                180.0,
                "Light-Blue"
            ),
            ActivityObject(
                1,
                0,
                "Programming",
                "College",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                40.0,
                120.0,
                "Red"
            ),
            ActivityObject(
                2,
                0,
                "Research",
                "College",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                120.0,
                240.0,
                "Pink"
            ),
            ActivityObject(
                3,
                0,
                "Project Management",
                "College",
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
                30.0,
                60.0,
                "Blue"
            )
        )
    }
}