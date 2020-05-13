package at.tugraz.ist.sw20.mam3.cook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.RecipeDetailFragment

class RecipeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        /*
        val recipeService = RecipeService(this)
        recipeService.getRecipeById(recipeID!!, object : DataReadyListener<Recipe> {
            @SuppressLint("SetTextI18n")
            override fun onDataReady(data: Recipe?) {
                val recipe = data!!
                Log.println(Log.DEBUG, "CookDB", data.toString())
                //TODO as soon as images are supported update this
                // image_displayed_recipe.setBackgroundResource()
                recipe_title.text = recipe.name
                recipe_type.text = "#" + recipe.kind
                recipe_cook_time.findViewById<TextView>(R.id.recipe_time).text = "" + recipe.cookMinutes
                recipe_cook_time.findViewById<ImageView>(R.id.recipe_time_img).setImageResource(R.drawable.ic_cooking)
                recipe_prepare_time.findViewById<TextView>(R.id.recipe_time).text = "" + recipe.prepMinutes
                root.findViewById(R.id.list_recipes).adapter = InstructionAdapter(context!!, data ?: listOf(), activity!!)
            }
        })*/

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_edit_detail_recipe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit_recipe -> {
                val recipeDetailFragment = supportFragmentManager.fragments[0] as RecipeDetailFragment
                recipeDetailFragment.editRecipe()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}