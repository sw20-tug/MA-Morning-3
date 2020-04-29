package at.tugraz.ist.sw20.mam3.cook.ui.recipes

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.AddRecipeActivity
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RecipesFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var lvRecipes : ListView
    private lateinit var clickedRecipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recipes, container, false)
        lvRecipes = root.findViewById(R.id.list_recipes)
        registerForContextMenu(lvRecipes);
        val floatingButton: FloatingActionButton = root.findViewById(R.id.item_add_button)
        floatingButton.setOnClickListener {
            val intent = Intent(activity, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                activity!!.runOnUiThread {
                    lvRecipes.adapter = RecipeAdapter(context!!, data ?: listOf())
                }
            }
        }
        registerForContextMenu(lvRecipes);
        RecipeService(context!!).getAllRecipes(readyListener)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        val lv = v as ListView
        val acmi = menuInfo as AdapterContextMenuInfo
        clickedRecipe = lv.getItemAtPosition(acmi.position) as Recipe
        menu.add("Rename")
        menu.add("Edit")
        menu.add("Delete")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.toString().equals("Delete")) {
            val dialogBuilder = AlertDialog.Builder(activity!!)
                        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener{
                            dialog, id ->
                            RecipeService(context!!).deleteRecipe(clickedRecipe)
                            Toast.makeText(context!!, "deleted" ,Toast.LENGTH_LONG).show()
                            val readyListener = object : DataReadyListener<List<Recipe>> {
                                override fun onDataReady(data: List<Recipe>?) {
                                    activity!!.runOnUiThread {
                                        lvRecipes.adapter = RecipeAdapter(context!!, data ?: listOf())
                                    }
                                }
                            }
                            RecipeService(context!!).getAllRecipes(readyListener)
                        })
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                                dialog, id ->
                            dialog.dismiss()
                        })
            val alert = dialogBuilder.create()
            alert.setTitle("Are you sure you want to delete the recipe?")
            alert.show()
        }
        return super.onContextItemSelected(item)
    }
}