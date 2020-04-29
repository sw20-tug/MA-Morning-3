package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.IngredientAdapter
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.InstructionAdapter
import at.tugraz.ist.sw20.mam3.cook.ui.utils.AdaptListViewHeight
import kotlinx.android.synthetic.main.fragment_recipe_detail.*

class RecipeDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val recipeService = RecipeService(activity!!)
        val recipeID = activity!!.intent?.getLongExtra("recipeID", -1)
        Log.println(Log.INFO, "Parameter", "" + recipeID);
        recipeService.getRecipeById(recipeID!!, object : DataReadyListener<Recipe> {
            @SuppressLint("SetTextI18n")
            override fun onDataReady(data: Recipe?) {
                val recipe = data!!
                Log.println(Log.INFO, "Recipe", data.toString())
                Log.println(Log.INFO, "Steps", data.steps.toString())
                Log.println(Log.INFO, "Ingredients", data.ingredients.toString())
                //TODO as soon as images are supported update this
                // image_displayed_recipe.setBackgroundResource()


                recipe_title.text = recipe.name
                recipe_type.text = "#" + recipe.kind
                recipe_cook_time.findViewById<TextView>(R.id.recipe_time).text = "" + recipe.cookMinutes
                recipe_cook_time.findViewById<ImageView>(R.id.recipe_time_img).setImageResource(R.drawable.ic_cooking)
                recipe_prepare_time.findViewById<TextView>(R.id.recipe_time).text = "" + recipe.prepMinutes
                val photos = recipe.photos
                if (photos != null && photos.isNotEmpty()) {
                    val uri = RecipeService(context!!).loadImage(photos[0]);
                    root.findViewById<ImageView>(R.id.image_displayed_recipe).setImageURI(uri)
                } else {
                    root.findViewById<ImageView>(R.id.image_displayed_recipe).isGone = true
                }
                var lvIngredients = root.findViewById<RecyclerView>(R.id.recipe_ingredients)
                var lvInstructions = root.findViewById<RecyclerView>(R.id.recipe_instructions)
                lvIngredients.layoutManager = LinearLayoutManager(context)
                lvIngredients.isNestedScrollingEnabled = false
                lvIngredients.setHasFixedSize(true)

                lvInstructions.layoutManager = LinearLayoutManager(context)
                lvInstructions.isNestedScrollingEnabled = false
                lvInstructions.setHasFixedSize(true)


                lvInstructions.adapter = InstructionAdapter(context!!, recipe.steps ?: listOf(), activity!!)
                lvIngredients.adapter = IngredientAdapter(context!!, recipe.ingredients ?: listOf(), activity!!)
            }
        })
        return root
    }
}