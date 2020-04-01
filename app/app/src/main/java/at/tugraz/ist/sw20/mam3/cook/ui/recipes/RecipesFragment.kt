package at.tugraz.ist.sw20.mam3.cook.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipesFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    private lateinit var lvRecipes : ListView
    private lateinit var recipes : List<Recipe>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        recipesViewModel =
                ViewModelProvider(this).get(RecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recipes, container, false)
        lvRecipes = root.findViewById(R.id.list_recipes)

        val floatingButton: FloatingActionButton = root.findViewById(R.id.item_add_button)
        floatingButton.setOnClickListener {
            // val intent = Intent(this, )
            val fragment = AddRecipesFragment()
            val fragmentTransaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        //var recipe : Recipe = Recipe(-1, "Pizza","", "#Meat", 20, 30, false)
        //recipes = listOf(recipe, recipe, recipe, recipe, recipe, recipe, recipe, recipe)
        //lvRecipes.adapter = RecipeAdapter(this.context!!, recipes)

        val recipeDAO = CookDB.INSTANCE?.recipeDao()

        if(recipeDAO?.getAllRecipies() != null)
            lvRecipes.adapter = RecipeAdapter(this.context!!, recipeDAO.getAllRecipies())
        else
            lvRecipes.adapter = RecipeAdapter(this.context!!, listOf())

    }
}
