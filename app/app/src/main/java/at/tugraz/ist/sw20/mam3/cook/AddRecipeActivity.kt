package at.tugraz.ist.sw20.mam3.cook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment


class AddRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        customizeActionBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_add_edit_recipe, menu)
        return true
    }

    private fun customizeActionBar() {
        supportActionBar?.title = getString(R.string.title_add_recipes)
        if (intent.getLongExtra(AddRecipesFragment.INTENT_EXTRA_RECIPE_ID, -1L) > 0) {
            supportActionBar?.title = getString(R.string.title_edit_recipe)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_recipe -> {
                val addRecipeFragment = supportFragmentManager.fragments[0] as AddRecipesFragment
                addRecipeFragment.saveRecipe()
            }
            android.R.id.home -> {
                cleanup()
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        cleanup()
        super.onBackPressed()
    }

    fun cleanup() {
        RecipeService(this).cancelPendingDeletes()
    }
}