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
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.item_dropdown_input.view.*
import kotlinx.android.synthetic.main.item_time_input.view.*


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

        menu.add(activity!!.getString(R.string.recipe_option_edit))
        menu.add(activity!!.getString(R.string.recipe_option_delete))
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.toString() == activity!!.getString(R.string.recipe_option_delete)) {
            startDeleteRecipe()
        }
        else if (item.toString() == activity!!.getString(R.string.recipe_option_edit)) {
            startEditRecipe()
        }
        return super.onContextItemSelected(item)
    }

    private fun startDeleteRecipe() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        dialogBuilder.setPositiveButton(activity!!.getString(
            R.string
                .delete_button_text
        ), DialogInterface.OnClickListener {dialog, id ->

            val recipeListReadyListener = object : DataReadyListener<List<Recipe>> {
                override fun onDataReady(data: List<Recipe>?) {
                    activity!!.runOnUiThread {
                        lvRecipes.adapter = RecipeAdapter(
                            context!!, data ?: listOf(), activity!!, this@RecipesFragment
                        )
                    }
                }
            }

            val deleteFinishedListener = object : DataReadyListener<Boolean> {
                override fun onDataReady(data: Boolean?) {
                    RecipeService(context!!).getAllRecipes(recipeListReadyListener)
                }
            }

            RecipeService(context!!).deleteRecipe(clickedRecipe, deleteFinishedListener)

            Toast.makeText(context!!, getString(R.string.confirm_deleted_notification), Toast.LENGTH_LONG).show()
        })
        .setNegativeButton(activity!!.getString(R.string.cancel_button_text),
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
        })

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.confirm_delete_message))
        alert.show()
    }

    private fun startEditRecipe() {
        val intent = Intent(activity, AddRecipeActivity::class.java)
        intent.putExtra(AddRecipesFragment.INTENT_EXTRA_RECIPE_ID, clickedRecipe.recipeID)
        context!!.startActivity(intent)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val itemSearch = menu?.findItem(R.id.search) as MenuItem
        val searchView = itemSearch.getActionView() as SearchView
        val itemFilter = menu?.findItem(R.id.filter) as MenuItem
        itemFilter.actionView.setBackgroundResource(R.drawable.ic_filter_white)
        searchView.isIconifiedByDefault = false
        searchView.requestFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

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
            val more = itemFilter.getActionView() as ImageButton
            this.activity?.runOnUiThread(java.lang.Runnable {
                more.setOnClickListener {

                    val builder = AlertDialog.Builder(context!!)
                    val cv = layoutInflater.inflate(R.layout.dialog_filter, null) as View

                    builder.setView(cv)
                    builder.setTitle(getString(R.string.filter_title))

                    val dialog = builder.create()

                    setupDropdownMenus(cv.findViewById(R.id.filter_dropdown_type), R.array.types, null)
                    setupDropdownMenus(cv.findViewById(R.id.filter_dropdown_difficulty), R.array.skillLevel, null)
                    cv.findViewById<TextView>(R.id.filter_dropdown_type).dropdown_input_description.text = getString(R.string.create_edit_recipes_type)
                    cv.findViewById<TextView>(R.id.filter_dropdown_difficulty).dropdown_input_description.text = getString(R.string.create_edit_recipes_difficulty)
                    cv.findViewById<TextView>(R.id.filter_prep_time).time_input_description.text = getString(R.string.filter_max_prep_time_short)
                    cv.findViewById<TextView>(R.id.filter_cook_time).time_input_description.text = getString(R.string.filter_max_cook_time_short)
                    cv.findViewById<TextView>(R.id.filter_prep_time).time_input_minutes.text = getString(R.string.minutes_text_label)
                    cv.findViewById<TextView>(R.id.filter_cook_time).time_input_minutes.text = getString(R.string.minutes_text_label)

                    cv.findViewById<Button>(R.id.filter_button_clear_filters).setOnClickListener {
                        var list: ListView = lvRecipes
                        list!!.adapter = RecipeAdapter(context!!, listv, activity!!, this@RecipesFragment)
                        dialog.dismiss()
                    }

                    cv.findViewById<Button>(R.id.filter_button_ok).setOnClickListener {
                        var tmp_type: MutableList<Recipe> = mutableListOf()
                        var tmp_diff: MutableList<Recipe> = mutableListOf()
                        var tmp_prep: MutableList<Recipe> = mutableListOf()
                        var tmp_cook: MutableList<Recipe> = mutableListOf()
                        var list: ListView = lvRecipes

                        val type = cv.findViewById<Spinner>(R.id.filter_dropdown_type).dropdown_input_inputfield.selectedItem.toString()
                        val difficulty = cv.findViewById<Spinner>(R.id.filter_dropdown_difficulty).dropdown_input_inputfield.selectedItem.toString()
                        val prepTime = cv.findViewById<TextView>(R.id.filter_prep_time).time_input_inputfield.text.toString()
                        val cookTime = cv.findViewById<TextView>(R.id.filter_cook_time).time_input_inputfield.text.toString()


                        for (item in listv) {
                            if (item.kind == type) {
                                tmp_type.add(item)
                            }
                        }
                        for (item in tmp_type) {
                            if (item.difficulty == difficulty) {
                                tmp_diff.add(item)
                            }
                        }
                        if (prepTime.isBlank() || (prepTime.toInt() < 0)) {
                            tmp_prep = tmp_diff
                        } else {
                            for (item in tmp_diff) {
                                if (item.prepMinutes <= prepTime.toInt()) {
                                    tmp_prep.add(item)
                                }
                            }
                        }
                        if (cookTime.isBlank() || (cookTime.toInt() < 0)) {
                            tmp_cook = tmp_prep
                        } else {
                            for (item in tmp_prep) {
                                if (item.cookMinutes <= cookTime.toInt()) {
                                    tmp_cook.add(item)
                                }
                            }
                        }
                        list!!.adapter = RecipeAdapter(context!!, tmp_cook, activity!!, this@RecipesFragment)
                        dialog.dismiss()
                    }

                    dialog.show()
                }
            })
        }).start()
    }

    private fun setupDropdownMenus(root: View, arrayList: Int, selectedItem: String?) {
        val items = resources.getStringArray(arrayList)
        val spinner: Spinner = root.findViewById(R.id.dropdown_input_inputfield)

        if (selectedItem == null) {
            val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, items)
            spinner.adapter = adapter
        } else {
            spinner.setSelection(items.indexOf(selectedItem))
        }
    }

}