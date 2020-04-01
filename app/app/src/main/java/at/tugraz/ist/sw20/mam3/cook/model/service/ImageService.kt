package at.tugraz.ist.sw20.mam3.cook.model.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import java.io.File
import java.io.FileOutputStream

object ImageService {
    private const val mainDirName = "recipes"
    private const val tempDirName = "tmp"
    private var imageNameCounter : Int = 0

    fun storeImageTemporary(context : Context, imageUri : Uri) : Uri {
        val dir = File(context.filesDir, mainDirName).resolve(tempDirName)
        dir.mkdirs()

        val imgName = "img_" + imageNameCounter++ + ".jpg"
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val outFile = File(dir, imgName)
        outFile.createNewFile()
        val fos = FileOutputStream(outFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        return outFile.toUri()
    }

    fun storeImage(context : Context, recipe: Recipe) {

    }

    fun deleteImageTemporary() {

    }

    fun deleteImage() {

    }

    fun loadImage() {

    }
}