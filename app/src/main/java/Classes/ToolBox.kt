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
    }
}