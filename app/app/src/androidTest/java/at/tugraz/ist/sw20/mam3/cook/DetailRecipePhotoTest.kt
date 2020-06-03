package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import org.hamcrest.core.IsAnything.anything
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DetailRecipePhotoTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!
    private var recipeID = -1L

    @Before
    fun setup() {
        recipeDao = db.recipeDao()
        createRecipes()
    }

    private fun createImage(width: Int, height: Int, color: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.setColor(color)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

    private fun createRecipes() {
        val recipe = Recipe(1, "Burger", "...", "fast food","...",
            30, 10,true)
        recipeID = recipeDao.insertRecipe(recipe)
        val ingredient = Ingredient(0, recipeID, "TestIngredient")
        val step = Step(0, recipeID, "TestStep")
        recipeDao.insertIngredient(ingredient)
        recipeDao.insertStep(step)
        val bitmap1 = createImage(1000, 1000, 0xff0000)
        val bitmap2 = createImage(1000, 1000, 0x00ff00)
        val bitmap3 = createImage(1000, 1000, 0x0000ff)

        val context = ApplicationProvider.getApplicationContext<Context>()
        val recipeService = RecipeService(context)
        recipeService.storeImageTemporary(bitmap1!!)
        recipeService.storeImageTemporary(bitmap2!!)
        recipeService.storeImageTemporary(bitmap3!!)

        recipeService.storeImages(recipeID, null)
    }

    @After
    @Throws(IOException::class)
    fun cleanup() {
        db.close()
    }

    @Test
    fun showPicturesInDetailView() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.recipe_title)).perform(click())

        onData(anything()).inAdapterView(withId(R.id.recipe_images)).atPosition(0)
            .onChildView(withId(R.id.recipe_image)).check(matches(isDisplayed()))
    }
}


