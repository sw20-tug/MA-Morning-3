package at.tugraz.ist.sw20.mam3.cook.ui.recipes

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
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
    private var listv: List<Recipe> = emptyList()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                if (data != null) {
                    listv = data
                    activity!!.runOnUiThread {
                        lvRecipes.adapter = RecipeAdapter(context!!, data ?: listOf(), activity!!, this@RecipesFragment)
                    }
                }
            }
        }
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
                                        lvRecipes.adapter = RecipeAdapter(context!!, data ?: listOf(), activity!!, this@RecipesFragment)
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        val si = menu?.findItem(R.id.search) as MenuItem
        val sv = si.getActionView() as SearchView
        val ti = menu?.findItem(R.id.filter) as MenuItem
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var tmp: MutableList<Recipe> = mutableListOf<Recipe>()
                val list = lvRecipes

                if (newText!!.isNotEmpty()) {
                    for (item in listv) {
                        if (item.name.toLowerCase().contains(newText!!.toLowerCase())) {
                            tmp.add(item)
                        }
                        list!!.adapter = RecipeAdapter(context!!, tmp, activity!!, this@RecipesFragment)
                    }
                } else {
                    list!!.adapter = RecipeAdapter(context!!, listv, activity!!, this@RecipesFragment)
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
                    var filters = resources.getStringArray(R.array.s_item)

                    val checkedItems = booleanArrayOf(false, false, false, false, false, false)
                    builder.setMultiChoiceItems(filters, checkedItems) { dialog, which, isChecked ->

                    }

                    builder.setPositiveButton("OK") { dialog, which ->
                        var tmp: MutableList<Recipe> = mutableListOf()
                        var list: ListView = lvRecipes

                        val checked =
                            (dialog as AlertDialog).listView
                                .checkedItemPositions
                        if ((!checked[0] and checked[1]) or (checked[0] and !checked[1])) {
                            if (checked[0]) {
                                for (item in listv) {
                                    if (item.kind.equals("Meat")) {
                                        tmp.add(item)
                                    }
                                }
                            } else {
                                for (item in listv) {
                                    if (item.kind.equals("Side")) {
                                        tmp.add(item)
                                    }
                                }
                            }
                            if (checked[2] and !checked[3]) tmp = filterByCookMinutes(tmp, true)
                            if (!checked[2] and checked[3]) tmp = filterByCookMinutes(tmp, false)
                            if (checked[4] and !checked[5]) tmp = filterByPrepMinutes(tmp, true)
                            if (!checked[4] and checked[5]) tmp = filterByPrepMinutes(tmp, false)
                            list!!.adapter = RecipeAdapter(context!!, tmp, activity!!, this@RecipesFragment)
                        } else {
                            var tmp: MutableList<Recipe> = listv as MutableList<Recipe>
                            if (checked[2] and !checked[3]) tmp = filterByCookMinutes(tmp, true)
                            if (!checked[2] and checked[3]) tmp = filterByCookMinutes(tmp, false)
                            if (checked[4] and !checked[5]) tmp = filterByPrepMinutes(tmp, true)
                            if (!checked[4] and checked[5]) tmp = filterByPrepMinutes(tmp, false)
                            list!!.adapter = RecipeAdapter(context!!, tmp, activity!!, this@RecipesFragment)
                        }
                    }
                    builder.setNegativeButton("Cancel", null)

                    val dialog = builder.create()
                    dialog.show()
                }
            })
        }).start()
    }

    private fun filterByCookMinutes(list: MutableList<Recipe>, less: Boolean): MutableList<Recipe> {
        var tmp: MutableList<Recipe> = mutableListOf()
        if (less) {
            for (item in list) {
                if (item.cookMinutes < 30) {
                    tmp.add(item)
                }
            }
        } else {
            for (item in list) {
                if (item.cookMinutes >= 30) {
                    tmp.add(item)
                }
            }
        }
        return tmp
    }

    private fun filterByPrepMinutes(list: MutableList<Recipe>, less: Boolean): MutableList<Recipe> {
        var tmp: MutableList<Recipe> = mutableListOf()
        if (less) {
            for (item in list) {
                if (item.prepMinutes < 15) {
                    tmp.add(item)
                }
            }
        } else {
            for (item in list) {
                if (item.prepMinutes >= 15) {
                    tmp.add(item)
                }
            }
        }
        return tmp
    }

}