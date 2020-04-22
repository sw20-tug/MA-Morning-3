package at.tugraz.ist.sw20.mam3.cook.ui.recipes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
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
        recipesViewModel =
            ViewModelProvider(this).get(RecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recipes, container, false)
        lvRecipes = root.findViewById(R.id.list_recipes)
        registerForContextMenu(lvRecipes);
        val floatingButton: FloatingActionButton = root.findViewById(R.id.item_add_button)
        floatingButton.setOnClickListener {
            // val intent = Intent(this, )
            val fragment = AddRecipesFragment()
            val fragmentTransaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            val lv = lvRecipes;
            lvRecipes.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
                val item = parent.getItemAtPosition(position) as Recipe
                registerForContextMenu(lv);
                true
            }
        }

        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                lvRecipes.adapter = RecipeAdapter(context!!, data ?: listOf())
            }
        }
//        lvRecipes.adapter = RecipeAdapter(context!!, listOf(
//            Recipe(1, "testrecipe", "testdescription", "meat", 5, 10, false),
//            Recipe(2, "testrecipe", "testdescription", "meat", 5, 10, false)))
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
//                        dialogBuilder.setCancelable(false)
                        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener{
                            dialog, id ->
//                            Toast.makeText(context!!, "deleted" ,Toast.LENGTH_LONG).show()
                            RecipeService(context!!).deleteRecipe(clickedRecipe)
                            dialog.dismiss()
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