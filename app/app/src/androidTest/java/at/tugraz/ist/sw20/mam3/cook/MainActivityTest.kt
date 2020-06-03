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
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
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

    private fun addTestRecipes() {
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
        onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(0).
            onChildView(withId(R.id.recipe_favourite_star)).perform(click())


        onView(withId(R.id.navigation_favourites)).perform(click())
        onView(withId(R.id.list_favorites)).check(matches(withListSize(1)))
        onData(anything()).inAdapterView(withId(R.id.list_favorites)).atPosition(0).
            onChildView(withId(R.id.recipe_title)).check(matches(withText("Burger")))


        onView(withId(R.id.navigation_recipes)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(3).
            onChildView(withId(R.id.recipe_favourite_star)).perform(click())

        onView(withId(R.id.navigation_favourites)).perform(click())
        onView(withId(R.id.list_favorites)).check(matches(withListSize(2)))
        onData(anything()).inAdapterView(withId(R.id.list_favorites)).atPosition(1).
            onChildView(withId(R.id.recipe_title)).check(matches(withText("Knödel")))

        onData(anything()).inAdapterView(withId(R.id.list_favorites)).atPosition(1).
            onChildView(withId(R.id.recipe_favourite_star)).perform(click())

        onView(withId(R.id.list_favorites)).check(matches(withListSize(1)))
    }
}