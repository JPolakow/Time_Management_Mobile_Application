package Classes

import android.graphics.Bitmap

object ImageManager {
    private var savedImage: Bitmap? = null

    fun saveImage(image: Bitmap) {
        savedImage = image
    }

    fun loadImage(): Bitmap? {
        return savedImage
    }
}