package Classes

import android.graphics.Bitmap
import java.util.*

class WorkEntriesObject(
    val WEActivityID: Int,
    val WEActivityName: String,
    val WEActivityCategory: String,
    val WEUserID: Int,
    val WERating: Int,
    val WEDateEnded: String,
    val WEDuration: String,
    val WEColor: String,
    ) {

    private var savedImage: Bitmap? = null

    fun saveImage(image: Bitmap) {
        savedImage = image
    }

    fun getSavedImage(): Bitmap? {
        return savedImage
    }

}