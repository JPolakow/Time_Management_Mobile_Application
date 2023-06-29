package Classes

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class ToolBox {
    companion object {
        var ActiveUserID: String = ""

        //used to store selected category
        var SelectedCategory: String = "None"

        //Array of Objects
        var WorkEntriesList = mutableListOf<WorkEntriesObject>(
        )

        var CategoryList = mutableListOf<CategoryObject>(
        )

        // Passwords are hashed and stored safely
        var UsersList = mutableListOf<UserClass>(
        )

        //@SuppressLint("ConstantLocale")
        var ActivitiesList = mutableListOf<ActivityObject>(
        )

        //Array of Objects
//        val WorkEntriesList = mutableListOf<WorkEntriesObject>(
//            WorkEntriesObject(1, "Name 1", "College", 0, 5, "06-06-2023", 4.0, "Red"),
//            WorkEntriesObject(1, "Name 2", "College", 0, 3, "07-06-2023", 9.1, "Red"),
//            WorkEntriesObject(1, "Name 3", "College", 0, 4, "08-06-2023", 11.8, "Red"),
//            WorkEntriesObject(1, "Name 4", "Hobby", 0, 4, "09-06-2023", 1.1, "Red"),
//            WorkEntriesObject(1, "Name 5", "Hobby", 0, 5, "10-06-2023", 0.1, "Red")
//        )
//
//        val CategoryList = mutableListOf<CategoryObject>(
//            CategoryObject("College", 0),
//            CategoryObject("Hobby", "")
//        )
//
//        // Passwords are hashed and stored safely
//        val UsersList = mutableListOf<UserClass>(
//            UserClass(
//                "user",
//                "John",
//                "Doe",
//                "D74FF0EE8DA3B9806B18C877DBF29BBDE50B5BD8E4DAD7A3A725000FEB82E8F1"
//            )
//        )
//
//        @SuppressLint("ConstantLocale")
//        val ActivitiesList = mutableListOf<ActivityObject>(
//            ActivityObject(
//                0,
//                0,
//                "Open-Source",
//                "College",
//                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
//                60.0,
//                180.0,
//                "Light-Blue",
//                "Android and Github fun"
//            ),
//            ActivityObject(
//                1,
//                0,
//                "Programming",
//                "College",
//                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
//                40.0,
//                120.0,
//                "Red",
//                "Life advice and C#"
//            ),
//            ActivityObject(
//                2,
//                0,
//                "Research",
//                "College",
//                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
//                120.0,
//                240.0,
//                "Pink",
//                "pain"
//            ),
//            ActivityObject(
//                3,
//                0,
//                "Project Management",
//                "College",
//                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date()),
//                30.0,
//                60.0,
//                "Blue",
//                "Team building and future knowledge"
//            )
//        )
    }
}