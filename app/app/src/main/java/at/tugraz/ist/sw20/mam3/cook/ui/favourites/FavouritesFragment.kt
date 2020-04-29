package at.tugraz.ist.sw20.mam3.cook.ui.favourites

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var lvFavorites: ListView
    private lateinit var clickedRecipe: Recipe

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
                ViewModelProvider(this).get(FavouritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        lvFavorites = root.findViewById(R.id.list_favorites)
        registerForContextMenu(lvFavorites);
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouritesViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                lvFavorites.adapter = RecipeAdapter(context!!, data ?: listOf())
            }
        }
        RecipeService(context!!).getFavoriteRecipes(readyListener)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        val lv = v as ListView
        val acmi = menuInfo as AdapterView.AdapterContextMenuInfo
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
                Toast.makeText(context!!, "deleted" , Toast.LENGTH_LONG).show()
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
