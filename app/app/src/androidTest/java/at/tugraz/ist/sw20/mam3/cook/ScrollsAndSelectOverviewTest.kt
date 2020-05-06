package at.tugraz.ist.sw20.mam3.cook

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.service.autofill.Validators.not
import androidx.navigation.NavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)

class ScrollsAndSelectOverviewTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)


    @Before
    fun setup() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        //db = CookDB.getTestDB(context)!!
        recipeDao = db.recipeDao()

        addTestRecipes()
    }

    fun addTestRecipes() {
        var recipe = Recipe(0, "Burger", "...", "...","...",
            10, 5,true)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Pizza", "...", "...","...",
            30, 10,true)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Pasta", "...", "...","...",
            1, 15,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Knödel", "...", "...","...",
            20, 10,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Schnitzel", "...", "...","...",
            15, 5,false)
        recipeDao.insertRecipe(recipe)

        recipe = Recipe(0, "Salami Pizza", "...", "...","...",
            40, 10,true)
        recipeDao.insertRecipe(recipe)
    }

    @After
    @Throws(IOException::class)
    fun cleanup() {
        db.close()
        CookDB.destroyDataBase()
    }

    @Test
    fun behaviorTest() {

        //onView(withId(R.id.button_add_recipes)).perform(click())
        onView(withId(R.id.navigation_recipes)).perform(click())

        Intents.intended(hasComponent(RecipeDetailActivity::class.java.getName()))
        //intentsTestRule.launchActivity(Intent())
        //Intents.intended(hasComponent(MainActivity::class.java.getName()))
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.list_recipes)).check(matches(isClickable()))
        onView(withId(R.id.filter)).check(matches(isClickable()))
        onView(withId(R.id.search)).check(matches(isClickable()))
        //onView(withId(R.id.settings)).check(matches(isClickable()))
        //onView(withId(R.id.navigation_recipes)).perform(click())

        //onView(withId(R.layout.fragment_recipes)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.button_add_recipes)).perform(click())
        Intents.intended(hasComponent(AddRecipeActivity::class.java.getName()))



        Intents.release()
        //onData(allOf(`is`(instanceOf(String::class.java)), `is`("Schnitzel"))).inAdapterView(withId(R.id.list_recipes)).perform(click())
        //onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(5).perform(click())
        //onData(anything()).inAdapterView(withId(R.id.list_recipes)).atPosition(5).perform(swipeUp())
    }

    @Test
    fun test() {
    }

}