package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleRecipePhotoReadWriteTest {
    private lateinit var recipeDao: RecipeDAO
    private lateinit var db: CookDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, CookDB::class.java).build()
        recipeDao = db.recipeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertRecipePhoto() {
        val photo = RecipePhoto(0, 0)

        val rID = recipeDao.insertRecipePhoto(photo)

        assertEquals(rID, 1)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAllRecipePhoto() {
        val photo = RecipePhoto(0, 0)

        recipeDao.insertRecipePhoto(photo)
        recipeDao.insertRecipePhoto(photo)
        recipeDao.insertRecipePhoto(photo)
        recipeDao.insertRecipePhoto(photo)
        recipeDao.insertRecipePhoto(photo)

        val photos = recipeDao.getAllPhotosFromRecipe(0)

        assertEquals(photos.size, 5)
    }
}


