package at.tugraz.ist.sw20.mam3.cook.ui.recipes

import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RecipesFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var lvRecipes : ListView

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

        RecipeService(context!!).getAllRecipes(readyListener)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        val lv = v as ListView
        val acmi = menuInfo as AdapterContextMenuInfo
        val obj: Recipe = lv.getItemAtPosition(acmi.position) as Recipe
        menu.add("Rename")
        menu.add("Edit")
        menu.add("Delete")

    }

}