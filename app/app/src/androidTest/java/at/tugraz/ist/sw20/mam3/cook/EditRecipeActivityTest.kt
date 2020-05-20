package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)

class EditRecipeActivityTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!

    @Before
    fun setup() {
        recipeDao = db.recipeDao()

        var recipe = Recipe(0, "Burger", "...", "fast food",
            "Beginner", 10, 5,true)
        recipeDao.insertRecipe(recipe)
    }

    @After
    @Throws(IOException::class)
    fun cleanup() {
        db.close()
        CookDB.destroyDataBase()
    }

    @Rule // third parameter is set to false which means the activity is not started automatically
    var mActivityRule: ActivityTestRule<AddRecipeActivity> =
        ActivityTestRule(AddRecipeActivity::class.java, false, false)

    @Test
    fun editRecipe() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), AddRecipeActivity::class.java)
        intent.putExtra(AddRecipesFragment.INTENT_EXTRA_RECIPE_ID, 1)

        mActivityRule.launchActivity(intent)

        onView(withId(R.id.add_edit_recipe_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_scroll_view)).check(matches(isDisplayed()))
//        onView(allOf(withParent(withId(R.id.text_input_name)), withId(R.id.text_input_inputfield)))
//            .perform(typeText("Pizza"))
//        onView(allOf(withParent(withId(R.id.text_input_descr)), withId(R.id.text_input_inputfield)))
//            .perform(typeText("Blabla"))
//        onView(allOf(withParent(withId(R.id.text_input_preptime)), withId(R.id.time_input_inputfield)))
//            .perform(typeText("10"))
//        onView(allOf(withParent(withId(R.id.text_input_cooktime)), withId(R.id.time_input_inputfield)))
//            .perform(typeText("10"))
//        onView(allOf(withParent(withId(R.id.text_input_ingredients)), withId(R.id.ingredient_input_inputfield)))
//            .perform(typeText("Blabla"))
//        onView(allOf(withParent(withId(R.id.text_input_ingredients)), withId(R.id.ingredient_input_button)))
//            .perform(click())
//
//
//        onView(allOf(withParent(withId(R.id.text_input_instructions)), withId(R.id.instruction_input_inputfield)))
//            .perform(scrollTo(), typeText("Blabla"))
//        onView(allOf(withParent(withId(R.id.text_input_instructions)), withId(R.id.instruction_input_button)))
//            .perform(scrollTo(), click())
//        onView(withId(R.id.action_save_recipe)).perform(click())
//        ActivityScenario.launch(MainActivity::class.java)
//
//
//        onView(withId(R.id.recipe_title)).check(matches(withText("Pizza")))
//        println()

    }

    @Test
    fun testElements() {
        val activityScenario = ActivityScenario.launch(AddRecipeActivity::class.java)

        onView(withId(R.id.add_edit_recipe_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_name)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_descr)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_type)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_scroll_view)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_difficulty)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_preptime)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_cooktime)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_ingredients)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_instructions)).check(matches(isDisplayed()))
        onView(withId(R.id.text_input_images)).check(matches(isDisplayed()))
    }

}