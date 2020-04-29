package at.tugraz.ist.sw20.mam3.cook.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
                ViewModelProvider(this).get(FavouritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        lvFavorites = root.findViewById(R.id.list_favorites)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouritesViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                lvFavorites.adapter = RecipeAdapter(context!!, data ?: listOf(), activity!!)
            }
        }

        RecipeService(context!!).getFavoriteRecipes(readyListener)
    }
}
