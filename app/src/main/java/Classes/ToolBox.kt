package Classes

class ToolBox {
    companion object {
        var ActiveUserID: String = ""

        // Used to store selected category
        var SelectedCategory: String = "None"

        // Array work entry objects
        var WorkEntriesList = mutableListOf<WorkEntriesObject>(
        )

        var CategoryList = mutableListOf<CategoryObject>(
        )

        // Passwords are hashed and stored safely
        var UsersList = mutableListOf<UserClass>(
        )

        // Activity object list
        var ActivitiesList = mutableListOf<ActivityObject>(
        )
    }
}