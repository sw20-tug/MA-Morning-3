package at.tugraz.ist.sw20.mam3.cook.model.service

import android.content.Context
import android.util.Log
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toFile
import androidx.core.net.toUri
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class RecipeService(private val context: Context) {
    private var db: CookDB? = null //getter from singleton we created here

    private val mainDirName = "recipes"
    private val tempDirName = "tmp"
    private var imageNameCounter : Long = 0

    fun getAllRecipes(callback: DataReadyListener<List<Recipe>>) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val allRecipes = db!!.recipeDao().getAllRecipies()
            callback.onDataReady(allRecipes)
        }).start()
    }

    fun getFavouriteRecipes(callback: DataReadyListener<List<Recipe>>) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val allRecipes = db!!.recipeDao().getFavourites()
            callback.onDataReady(allRecipes)
        }).start()
    }

    fun addOrUpdateRecipe(recipe: Recipe, ingredients: List<Ingredient>, steps: List<Step>,
                          photos: List<RecipePhoto>, callback: DataReadyListener<Long>) {

        Thread(Runnable {
            db = CookDB.getCookDB(context)

            val rID: Long

            if (recipe.recipeID == 0L) {
                rID = db!!.recipeDao().insertRecipe(recipe)

            }
            else {
                db!!.recipeDao().updateRecipe(recipe)
                rID = recipe.recipeID

                recipe.ingredients?.forEach { db!!.recipeDao().deleteIngredient(it) }
                recipe.steps?.forEach { db!!.recipeDao().deleteStep(it) }
                recipe.photos?.forEach {
                    deleteImage(it)
                    db!!.recipeDao().deleteRecipePhoto(it)
                }
            }

            for (ingredient in ingredients) {
                ingredient.recipeID = rID
                db!!.recipeDao().insertIngredient(ingredient)
            }

            for (step in steps) {
                step.stepID = 0
                step.recipeID = rID
                db!!.recipeDao().insertStep(step)
            }

            //TODO test if this works once photos are a thing
//            storeImages(rID, null)

            callback.onDataReady(rID)
        }).start()
    }

    fun getRecipeById(id: Long, callback: DataReadyListener<Recipe>) {
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val recipe = db!!.recipeDao().getRecipeById(id)
            recipe.ingredients = db!!.recipeDao().getIngredientsByRecipeID(id)
            Log.i("INGREDIENTS DATABASE SERVICE", recipe.ingredients.toString())
            recipe.steps = db!!.recipeDao().getStepsByRecipeID(id)
            Log.i("STEPS DATABASE SERVICE", recipe.steps.toString())
            recipe.photos = db!!.recipeDao().getAllPhotosFromRecipe(recipe.recipeID)
            callback.onDataReady(recipe)
        }).start()
    }

    // Callback function call when finished
    fun deleteRecipe(recipe: Recipe, callback: DataReadyListener<Boolean>? = null) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val readyListener = object : DataReadyListener<List<RecipePhoto>> {
                override fun onDataReady(data: List<RecipePhoto>?) {
                    for (image in data!!) {
                        deleteImage(image);
                        db!!.recipeDao().deleteRecipePhoto(image);
                    }
                    db!!.recipeDao().deleteRecipe(recipe)

                    callback?.onDataReady(true)
                }
            }
            getAllPhotosFromRecipe(recipe, readyListener)
        }).start()
    }

    fun setRecipeFavourite(recipe: Recipe, isFavourite: Boolean) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            db!!.recipeDao().setRecipeFavourite(recipe.recipeID, isFavourite)
        }).start()
    }

    fun deleteStep(step: Step, callback: DataReadyListener<Boolean>? = null) {
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            db!!.recipeDao().deleteStep(step)
            callback?.onDataReady(true)
        }).start()
    }

    fun updateStep(step: Step, callback: DataReadyListener<Boolean>? = null) {
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            db!!.recipeDao().updateStep(step)
            callback?.onDataReady(true)
        }).start()
    }

    fun getAllPhotosFromRecipe(recipe: Recipe, callback: DataReadyListener<List<RecipePhoto>>) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val allPhotos = db!!.recipeDao().getAllPhotosFromRecipe(recipe.recipeID)
            callback.onDataReady(allPhotos)
        }).start()
    }

    fun storeImageTemporary(imageUri : Uri) : Uri {
        return storeImageTemporary(MediaStore.Images.Media.getBitmap(context.contentResolver,
            imageUri))
    }

    fun storeImageTemporary(imageBitmap : Bitmap) : Uri {
        val dir = File(context.filesDir, mainDirName).resolve(tempDirName)
        dir.mkdirs()

        val imgName = getImageName(imageNameCounter++)
        val outFile = File(dir, imgName)
        outFile.createNewFile()
        val fos = FileOutputStream(outFile)
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        return outFile.toUri()
    }

    fun storeImages(recipeID: Long, callback: DataReadyListener<Unit>?) {
        val srcDir = File(context.filesDir, mainDirName).resolve(tempDirName)
        val destDir = File(context.filesDir, mainDirName).resolve(recipeID.toString())

        if (!srcDir.exists()) {
            throw FileNotFoundException(tempDirName + " dir does not exist. Why do we want to " +
                "store anything that does not exist?")
        }

        destDir.mkdirs()
        Thread(Runnable {
            db = CookDB.getCookDB(context)

            for (image in srcDir.listFiles()!!) {
                val recipePhotoId = db!!.recipeDao().insertRecipePhoto(RecipePhoto(0,
                    recipeID))
                val imgName = getImageName(recipePhotoId)
                image.copyTo(File(destDir, imgName))
                image.delete()
            }
            callback?.onDataReady(null)
        }).start()
    }

    fun deleteTemporaryImages() {
        val srcDir = File(context.filesDir, mainDirName).resolve(tempDirName)
        if (srcDir.exists()) {
            for (file in srcDir.listFiles()!!) {
                file.delete()
            }
        }
    }

    fun deleteTemporaryImage(imageUri : Uri) {
        imageUri.toFile().delete()
    }

    fun deleteImage(recipePhoto : RecipePhoto) {
        val recipeDir = File(context.filesDir, mainDirName).resolve(recipePhoto.recipeID.toString())
        recipeDir.resolve(
            getImageName(recipePhoto.photoID)).delete()

        if (recipeDir.listFiles()!!.isEmpty()) {
            recipeDir.delete()
        }
    }

    fun loadImage(recipePhoto : RecipePhoto) : Uri {
        return File(context.filesDir, mainDirName).resolve(recipePhoto.photoID.toString()).resolve(
            getImageName(recipePhoto.photoID)).toUri()
    }

    private fun getImageName(recipePhotoId : Long) : String {
        return "img_$recipePhotoId.jpg"
    }

}