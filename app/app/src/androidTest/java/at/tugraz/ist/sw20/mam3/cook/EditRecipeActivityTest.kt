package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.IsAnything
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)

class EditRecipeActivityTest {
    private lateinit var recipeDao: RecipeDAO
    private var db: CookDB = CookDB.getTestDB(ApplicationProvider.getApplicationContext<Context>())!!
    private var recipeID = -1L

    @Before
    fun setup() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        //db = CookDB.getTestDB(context)!!
        recipeDao = db.recipeDao()

        val recipe = Recipe(0, "Pizza", "...", "fast food","...",
            30, 10,true)
        recipeID = recipeDao.insertRecipe(recipe)
        val ingredient = Ingredient(0, recipeID, "TestIngredient")
        val step = Step(0, recipeID, "TestStep")
        recipeDao.insertIngredient(ingredient)
        recipeDao.insertStep(step)
    }


    @After
    @Throws(IOException::class)
    fun cleanup() {
        db.close()
        CookDB.destroyDataBase()
    }

    @Test
    fun editRecipeViaDialog() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recipe_title)).perform(longClick())
        onView(withText("Edit")).check(matches(isDisplayed())).perform(click())

        performEditRecipe()
    }

    @Test
    fun editRecipeViaDetailView() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recipe_title)).perform(click())
        onView(withId(R.id.action_edit_recipe)).perform(click())

        performEditRecipe()
    }

    private fun performEditRecipe() {
        onView(withId(R.id.add_edit_recipe_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_scroll_view)).check(matches(isDisplayed()))

        onView(allOf(withParent(withId(R.id.text_input_name)), withId(R.id.text_input_inputfield)))
            .perform(replaceText("PizzaEdited"))

        onView(allOf(withParent(withId(R.id.text_input_descr)), withId(R.id.text_input_inputfield)))
            .perform(replaceText("Edited Pizza"))

        onView(allOf(withParent(withId(R.id.text_input_preptime)),
                withId(R.id.time_input_inputfield))).perform(replaceText("11"))

        onView(allOf(withParent(withId(R.id.text_input_cooktime)),
                withId(R.id.time_input_inputfield)))
            .perform(replaceText("11"))

        onView(withId(R.id.ingredient_input_inputfield))
            .perform(replaceText("Edited Step"))

        onView(allOf(withParent(withId(R.id.text_input_ingredients)),
                withId(R.id.ingredient_input_button))).perform(click())


        onView(allOf(withParent(withId(R.id.text_input_instructions)),
                withId(R.id.instruction_input_inputfield)))
            .perform(scrollTo(), replaceText("Replaced instruction"))

        onView(allOf(withParent(withId(R.id.text_input_instructions)),
                withId(R.id.instruction_input_button))).perform(scrollTo(), click())

        onView(withId(R.id.action_save_recipe)).perform(click())

        onView(withId(R.id.recipe_title)).check(matches(withText("PizzaEdited")))
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