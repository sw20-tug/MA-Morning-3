package at.tugraz.ist.sw20.mam3.cook

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class AddRecipeActivityTest {

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