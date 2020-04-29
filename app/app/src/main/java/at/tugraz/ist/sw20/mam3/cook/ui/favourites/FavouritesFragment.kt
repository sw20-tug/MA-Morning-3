package at.tugraz.ist.sw20.mam3.cook.ui.favourites

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
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
    private var lv : List<Recipe> = emptyList()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouritesViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                lvFavorites.adapter = RecipeAdapter(context!!, data ?: listOf())
                if (data != null) {
                    lv = data
                }
            }
        }

        RecipeService(context!!).getFavoriteRecipes(readyListener)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val si = menu?.findItem(R.id.search) as MenuItem
        val sv = si.getActionView() as SearchView
        val ti = menu?.findItem(R.id.filter) as MenuItem

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var tmp : MutableList<Recipe> = mutableListOf<Recipe>()
                val list = lvFavorites

                if(newText!!.isNotEmpty()) {
                    for (item in lv) {
                        if (item.name.toLowerCase().contains(newText!!.toLowerCase())) {
                            tmp.add(item)
                        }
                        list!!.adapter = RecipeAdapter(context!!, tmp)
                    }
                }
                else {
                    list!!.adapter = RecipeAdapter(context!!, lv)
                }
                return true
            }
        })

        Thread(Runnable {
            val more = ti.getActionView() as ImageButton
            this.activity?.runOnUiThread(java.lang.Runnable {
                more.setOnClickListener {

                    val builder = AlertDialog.Builder(context!!)
                    builder.setTitle("Choose filters")

                    val filters = arrayOf("Meat", "Side", "Cooking < 30 minutes", "Cooking >= 30 minutes", "Preparation < 15 minutes", "Preparation >= 15 minutes")
                    val checkedItems = booleanArrayOf(false, false, false, false, false, false)
                    builder.setMultiChoiceItems(filters, checkedItems) { dialog, which, isChecked ->

                    }

                    builder.setPositiveButton("OK") { dialog, which ->
                        var tmp : MutableList<Recipe> = mutableListOf()
                        var list : ListView = lvFavorites

                        val cheCked =
                            (dialog as AlertDialog).listView
                                .checkedItemPositions
                        if ((!cheCked[0] and cheCked[1]) or (cheCked[0] and !cheCked[1])) {
                            if(cheCked[0]) {
                                for (item in lv) {
                                    if (item.kind.equals("Meat")) {
                                        tmp.add(item)
                                    }
                                }
                            }
                            else {
                                for (item in lv) {
                                    if (item.kind.equals("Side")) {
                                        tmp.add(item)
                                    }
                                }
                            }
                            if (cheCked[2] and !cheCked[3]) tmp = filterByCookMinutes(tmp, true)
                            if (!cheCked[2] and cheCked[3]) tmp = filterByCookMinutes(tmp, false)
                            if (cheCked[4] and !cheCked[5]) tmp = filterByPrepMinutes(tmp, true)
                            if (!cheCked[4] and cheCked[5]) tmp = filterByPrepMinutes(tmp, false)
                            list!!.adapter = RecipeAdapter(context!!, tmp)
                        }

                        else {
                            var tmp : MutableList<Recipe> = lv as MutableList<Recipe>
                            if (cheCked[2] and !cheCked[3]) tmp = filterByCookMinutes(tmp, true)
                            if (!cheCked[2] and cheCked[3]) tmp  = filterByCookMinutes(tmp, false)
                            if (cheCked[4] and !cheCked[5]) tmp = filterByPrepMinutes(tmp, true)
                            if (!cheCked[4] and cheCked[5]) tmp = filterByPrepMinutes(tmp, false)
                            list!!.adapter = RecipeAdapter(context!!, tmp)
                        }
                    }
                    builder.setNegativeButton("Cancel", null)

                    val dialog = builder.create()
                    dialog.show()
                }
            })
        }).start()
    }

    private fun filterByCookMinutes (list: MutableList<Recipe>, less: Boolean): MutableList<Recipe> {
        var tmp : MutableList<Recipe> = mutableListOf()
        if (less) {
            for (item in list) {
                if (item.cookMinutes < 30) {
                    tmp.add(item)
                }
            }
        }
        else {
            for (item in list) {
                if (item.cookMinutes >= 30) {
                    tmp.add(item)
                }
            }
        }
        return tmp
    }
    private fun filterByPrepMinutes (list: MutableList<Recipe>, less: Boolean)  : MutableList<Recipe> {
        var tmp : MutableList<Recipe> = mutableListOf()
        if (less) {
            for (item in list) {
                if (item.prepMinutes < 15) {
                    tmp.add(item)
                }
            }
        }
        else {    for (item in list) {
            if (item.prepMinutes >= 15) {
                tmp.add(item)
            }
        }
        }
        return tmp
    }

}