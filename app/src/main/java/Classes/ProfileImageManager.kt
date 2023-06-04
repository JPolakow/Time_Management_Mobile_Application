package Classes

import android.graphics.Bitmap

object ProfileImageManager {
    private var savedImage: Bitmap? = null

    fun saveImage(image: Bitmap) {
        savedImage = image
    }

    fun loadImage(): Bitmap? {
        return savedImage
    }
}