package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import android.view.View
import android.widget.ListView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsAnything.anything
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)


class OnSiteCookingActivityTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!
    private var recipeID = -1L

    @Before
    fun setup() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        //db = CookDB.getTestDB(context)!!
        recipeDao = db.recipeDao()

        addTestRecipes()
    }

    private fun addTestRecipes() {

        val recipe = Recipe(0, "Pizza", "...", "fast food","...",
            30, 10,true)
        recipeID = recipeDao.insertRecipe(recipe)
        val ingredient = Ingredient(0, recipeID, "TestIngredient")
        val step = Step(0, recipeID, "TestStep")
        val step1 = Step(0, recipeID, "steppp")
        val step2 = Step(0, recipeID, "testing")
        recipeDao.insertIngredient(ingredient)
        recipeDao.insertStep(step)
        recipeDao.insertStep(step1)
        recipeDao.insertStep(step2)

    }

    private fun withListSize (size: Int) : Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                return (view as ListView).count == size
            }

            override fun describeTo(description: org.hamcrest.Description?) {
                description?.appendText("ListView should have $size items")
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun cleanup() {
        db.close()
        CookDB.destroyDataBase()
    }

    @Test
    fun testElements() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.list_recipes)).perform(click())
        onView(withId(R.id.recipe_play_button)).perform(click())

        onView(withId(R.id.tv_onsite_step_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_onsite_step_content)).check(matches(isDisplayed()))

        onView(withId(R.id.btn_onsite_next)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_onsite_next)).perform(click())

        onView(withId(R.id.btn_onsite_back)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_onsite_next)).perform(click())

        onView(withId(R.id.btn_onsite_next)).check(matches(withText("Done")))
        onView(withId(R.id.btn_onsite_next)).perform(click())

        onView(withId(R.id.recipe_play_button)).check(matches(isDisplayed()))

    }
}