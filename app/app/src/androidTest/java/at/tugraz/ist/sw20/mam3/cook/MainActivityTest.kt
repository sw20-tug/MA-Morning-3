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

class MainActivityTest {

    @Test
    fun testElements() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.list_recipes)).check(matches(isClickable()))
        onView(withId(R.id.filter)).check(matches(isClickable()))
        onView(withId(R.id.search)).check(matches(isClickable()))
        onView(withId(R.id.button_add_recipes)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add_recipes)).perform(click())

    }

    @Test
    fun testFilterDialog() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.filter)).perform(click())
        onView(withText("Choose filters")).check(matches(isDisplayed()))

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
}