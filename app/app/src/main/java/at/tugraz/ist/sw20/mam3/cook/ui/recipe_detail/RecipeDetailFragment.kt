package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.AddRecipeActivity
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.ImageAdapter
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.IngredientAdapter
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.InstructionAdapter
import kotlinx.android.synthetic.main.fragment_recipe_detail.*

class RecipeDetailFragment : Fragment() {
    private var recipeID: Long? = -1L
    private lateinit var recipeService: RecipeService
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipeService = RecipeService(activity!!)
        root = inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        recipeID = activity!!.intent?.getLongExtra("recipeID", -1)
        Log.println(Log.INFO, "Parameter", "" + recipeID);
        return root
    }

    override fun onResume() {
        super.onResume()

        recipeService.getRecipeById(recipeID!!, object : DataReadyListener<Recipe> {
            @SuppressLint("SetTextI18n")
            override fun onDataReady(data: Recipe?) {
                val recipe = data!!
                activity!!.runOnUiThread {
                    Log.println(Log.INFO, "Recipe", recipe.toString())
                    Log.println(Log.INFO, "Steps", recipe.steps.toString())
                    Log.println(Log.INFO, "Ingredients", recipe.ingredients.toString())
                    Log.println(Log.INFO, "Images", recipe.photos.toString())
                    //TODO as soon as images are supported update this
                    // image_displayed_recipe.setBackgroundResource()


                    recipe_title.text = recipe.name
                    recipe_type.text = "#" + recipe.kind
                    recipe_cook_time.findViewById<TextView>(R.id.recipe_time).text =
                        "" + recipe.cookMinutes
                    recipe_cook_time.findViewById<ImageView>(R.id.recipe_time_img)
                        .setImageResource(R.drawable.ic_cooking)
                    recipe_prepare_time.findViewById<TextView>(R.id.recipe_time).text =
                        "" + recipe.prepMinutes
                    val photos = recipe.photos
                    if (photos != null && photos.isNotEmpty()) {
                        val uri = RecipeService(context!!).loadImage(photos[0]);
                        root.findViewById<ImageView>(R.id.image_displayed_recipe).setImageURI(uri)
                    } else {
                        root.findViewById<ImageView>(R.id.image_displayed_recipe).setImageResource(R.mipmap.sample_food_tacos_foreground)
                        root.findViewById<TextView>(R.id.recipe_images_text).isGone = true
                    }
                    val lvIngredients = root.findViewById<RecyclerView>(R.id.recipe_ingredients)
                    val lvInstructions = root.findViewById<RecyclerView>(R.id.recipe_instructions)
                    val lvImages = root.findViewById<RecyclerView>(R.id.recipe_images)

                    lvIngredients.layoutManager = LinearLayoutManager(context)
                    lvIngredients.isNestedScrollingEnabled = false
                    lvIngredients.setHasFixedSize(true)

                    lvInstructions.layoutManager = LinearLayoutManager(context)
                    lvInstructions.isNestedScrollingEnabled = false
                    lvInstructions.setHasFixedSize(true)

                    lvImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    lvImages.isNestedScrollingEnabled = true
                    lvImages.setHasFixedSize(false)

                    lvInstructions.adapter =
                        InstructionAdapter(context!!, recipe.steps ?: listOf())
                    lvIngredients.adapter =
                        IngredientAdapter(context!!, recipe.ingredients ?: listOf())
                    lvImages.adapter =
                        ImageAdapter(context!!, recipe.photos ?: listOf())
                }
            }
        })
    }

    fun editRecipe() {
        val intent = Intent(activity, AddRecipeActivity::class.java)
        intent.putExtra(AddRecipesFragment.INTENT_EXTRA_RECIPE_ID, recipeID)
        context!!.startActivity(intent)
    }
}