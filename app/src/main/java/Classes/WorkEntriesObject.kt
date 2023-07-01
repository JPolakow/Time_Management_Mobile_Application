package Classes

import android.graphics.Bitmap
import java.util.*

class WorkEntriesObject(
    var WEID: String,
    val WEActivityID: String,
    val WEActivityName: String,
    val WEActivityCategory: String,
    val WEUserID: String,
    val WERating: Int,
    val WEDateEnded: String,
    val WEDuration: Double,
    val WEColor: String,
    ) {

    private var savedImage: Bitmap? = null

    // Function to save image
    fun saveImage(image: Bitmap) {
        savedImage = image
    }


    // Function to get the saved image
    fun getSavedImage(): Bitmap? {
        return savedImage
    }

}