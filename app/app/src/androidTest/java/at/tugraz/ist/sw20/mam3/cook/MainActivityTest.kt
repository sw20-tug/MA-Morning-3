package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.IsAnything.anything
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class MainActivityTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!

    @Before
    fun setup() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        //db = CookDB.getTestDB(context)!!
        recipeDao = db.recipeDao()

        addTestRecipes()
    }

    fun addTestRecipes() {
        var recipe = Recipe(0, "Burger", "...", "fast food","...",
            10, 5,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Pizza", "...", "fast food","...",
            30, 10,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Pasta", "...", "...","...",
            1, 15,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Knödel", "...", "...","...",
            20, 10,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Schnitzel", "...", "Meat","...",
            15, 5,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Salami Pizza", "...", "fast food","...",
            40, 10,false)
        recipeDao.insertRecipe(recipe)
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
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.list_recipes)).check(matches(isClickable()))
        onView(withId(R.id.filter)).check(matches(isClickable()))
        onView(withId(R.id.search)).check(matches(isClickable()))
        onView(withId(R.id.button_add_recipes)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add_recipes)).perform(click())
        onView(withId(R.id.recipe_favourite_star)).check(matches(isClickable()))
    }

    @Test
    fun testFilterDialog() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.filter)).perform(click())
        onView(withText("Choose filters")).check(matches(isDisplayed()))
        onView(withText("Meat")).perform(click());
        onView(withText("OK")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(0).
        onChildView(withId(R.id.recipe_title)).
        check(matches(withText("Schnitzel")))
    }

    @Test
    fun testSearch() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(allOf(withId(R.id.search), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.search), isDisplayed())).perform(typeText("pizza"))
        onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(0).
        onChildView(withId(R.id.recipe_title)).
        check(matches(withText("Pizza")))
        onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(1).
        onChildView(withId(R.id.recipe_title)).
        check(matches(withText("Salami Pizza")))

    }

    @Test
    fun testOverviewFilter() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.list_recipes)).perform(click())
        onView(withId(R.id.fragment_recipe_detail)).check(matches(isDisplayed()))

        onView(withId(R.id.recipe_icons)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_prepare_time)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_cook_time)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_title)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_play_button)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_type)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_ingredients_text)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_instructions_text)).check(matches(isDisplayed()))
    }

    @Test
    fun testFavButton() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        val recipeItem = onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(0)
        recipeItem.onChildView(withId(R.id.recipe_favourite_star)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.list_favorites)).atPosition(0).onChildView(withId(R.id.recipe_title)).check(
            matches(withText("Burger")))
//        onView(withId(R.id.recipe_favourite_star)).
    }
}