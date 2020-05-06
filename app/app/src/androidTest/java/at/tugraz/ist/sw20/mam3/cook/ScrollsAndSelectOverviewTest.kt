package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.RecipesFragment
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.hasEntry
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.EnumSet.allOf


@RunWith(AndroidJUnit4::class)

class ScrollsAndSelectOverviewTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!

    @get:Rule
    val activityRule : ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun createDb() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        //db = CookDB.getTestDB(context)!!
        recipeDao = db.recipeDao()

        addTestRecipes()
    }

    fun addTestRecipes() {
        var recipe = Recipe(0, "Burger", "...", "...",
            10, 5,true)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Pizza", "...", "...",
            30, 10,true)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Pasta", "...", "...",
            1, 15,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Knödel", "...", "...",
            20, 10,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Schnitzel", "...", "...",
            15, 5,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Salami Pizza", "...", "...",
            40, 10,true)
        recipeDao.insertRecipe(recipe)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        CookDB.destroyDataBase()
    }

    @Test
    fun test() {
        //Thread.sleep(1000)
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.list_recipes)).check(matches(isClickable()))
        //onData(allOf(`is`(instanceOf(String::class.java)), `is`("Schnitzel"))).inAdapterView(withId(R.id.list_recipes)).perform(click())
        //onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(5).perform(click())
        //onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(5).perform(swipeUp())
    }
}