package Classes

class ToolBox {
    companion object {
        val ActiveUserID = Int
        val ActivitiesList = mutableListOf<ActivityObject>()
        val WorkEntriesList = mutableListOf<WorkEntriesObject>()
        val UsersList = mutableListOf<ActiveUserClass>(
            //default user, password = pass
            ActiveUserClass("Name", "Surname", "user", "D74FF0EE8DA3B9806B18C877DBF29BBDE50B5BD8E4DAD7A3A725000FEB82E8F1")
        )
    }
}