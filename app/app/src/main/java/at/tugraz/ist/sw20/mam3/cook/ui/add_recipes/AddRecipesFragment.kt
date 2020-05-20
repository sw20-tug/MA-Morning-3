package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters.InstructionAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_edit_recipe.view.*
import kotlinx.android.synthetic.main.item_ingredients_input.*


class AddRecipesFragment : Fragment() {

    companion object {
        const val INTENT_EXTRA_RECIPE_ID = "recipeID"
    }

    private lateinit var addRecipesViewModel: AddRecipesViewModel

    private lateinit var root: View

    private var steps: MutableList<Step> = mutableListOf()

    private var recipe: Recipe? = null

    private lateinit var recipeService: RecipeService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addRecipesViewModel = ViewModelProvider(this).get(AddRecipesViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_add_edit_recipe, container, false)

        recipeService= RecipeService(context!!)

        setViewLabels()

        setupDropdownMenus(root.text_input_type, R.array.types, null)
        setupDropdownMenus(root.text_input_difficulty, R.array.skillLevel, null)
        setupIngredients()
        setupInstructions()

        val recipeID = activity!!.intent?.getLongExtra(INTENT_EXTRA_RECIPE_ID,
            -1)

        if (recipeID!! > -1) {
            recipeService.getRecipeById(recipeID, object: DataReadyListener<Recipe> {
                override fun onDataReady(data: Recipe?) {
                    recipe = data!!
                    activity!!.runOnUiThread {
                        setRecipeValues()
                    }
                }
            })
        }

        return root
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        val menuInflater = activity!!.menuInflater
//        menuInflater.inflate(R.menu.toolbar_edit_detail_recipe, menu)
//    }

    private fun setViewLabels() {
        val textViewName: TextView = root.text_input_name.findViewById(R.id.text_input_description)
        textViewName.setText(R.string.create_edit_recipes_name)
        val textViewDescr: TextView =
            root.text_input_descr.findViewById(R.id.text_input_description)
        textViewDescr.setText(R.string.create_edit_recipes_descr)
        val textViewType: TextView =
            root.text_input_type.findViewById(R.id.dropdown_input_description)
        textViewType.setText(R.string.create_edit_recipes_type)
        val textViewDiff: TextView =
            root.text_input_difficulty.findViewById(R.id.dropdown_input_description)
        textViewDiff.setText(R.string.create_edit_recipes_difficulty)
        val textViewPrep: TextView =
            root.text_input_preptime.findViewById(R.id.time_input_description)
        textViewPrep.setText(R.string.create_edit_recipes_preptime)
        val textViewCook: TextView =
            root.text_input_cooktime.findViewById(R.id.time_input_description)
        textViewCook.setText(R.string.create_edit_recipes_cooktime)
        val textViewIngr: TextView =
            root.text_input_ingredients.findViewById(R.id.ingredient_input_description)
        textViewIngr.setText(R.string.create_edit_recipes_ingredients)
        val textViewInstr: TextView =
            root.text_input_instructions.findViewById(R.id.instruction_input_description)
        textViewInstr.setText(R.string.create_edit_recipes_instructions)
        val textViewImage: TextView = root.text_input_images.findViewById(R.id.image_input_description)
        textViewImage.setText(R.string.create_edit_recipes_fotos)

        val textViewPrepMin: TextView = root.text_input_preptime.findViewById(R.id.time_input_minutes)
        textViewPrepMin.setText(R.string.minutes_text_label)
        val textViewCookMin: TextView = root.text_input_cooktime.findViewById(R.id.time_input_minutes)
        textViewCookMin.setText(R.string.minutes_text_label)

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

    private fun setupIngredients() {
        val ingredientsInputButton: Button = root.findViewById(R.id.ingredient_input_button)
        ingredientsInputButton.setOnClickListener {
            val textView: TextInputEditText = root.text_input_ingredients.findViewById(
                R.id.ingredient_input_inputfield)

            if (textView.text.toString().isNotBlank()) {
                addChip(textView.text.toString())
                textView.text?.clear()
            }
        }

    }

    private fun addChip(text: String) {
        val chip = Chip(context!!)
        chip.text = text
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true

        chip.setOnCloseIconClickListener {
            TransitionManager.beginDelayedTransition(ingredient_input_chipGroup)
            ingredient_input_chipGroup.removeView(chip)
        }

        ingredient_input_chipGroup.addView(chip)
    }

    fun setupInstructions() {

        val lvInstructions = root.text_input_instructions
            .findViewById<ListView>(R.id.instruction_input_listView)

        val btnAdd = root.text_input_instructions
            .findViewById<MaterialButton>(R.id.instruction_input_button)

        val btnCancel = root.text_input_instructions
            .findViewById<MaterialButton>(R.id.instruction_cancel_button)

        lvInstructions.adapter = InstructionAdapter(context!!, steps)

        lvInstructions.setOnItemLongClickListener { parent, view, position, id ->
            val step = steps[position]
            if (step.stepID > 0) {
                recipeService.deleteStep(step)
            }

            steps.removeAt(position)

            (lvInstructions.adapter as InstructionAdapter).notifyDataSetChanged()
            true
        }

        val text = root.text_input_instructions
            .findViewById<TextInputEditText>(R.id.instruction_input_inputfield)

        var selectedStep: Step? = null

        lvInstructions.setOnItemClickListener { parent, view, position, id ->
            val step = steps[position]
            selectedStep = step
            text.setText(step.name)
            text.requestFocus()
            text.setSelection(step.name.length)
            val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            inputMethodManager.showSoftInput(text, InputMethodManager.SHOW_IMPLICIT)

            btnAdd.text = getString(R.string.save_button_text)

            btnCancel.visibility = View.VISIBLE
        }

        btnAdd.setOnClickListener {
            val stepText = text.text.toString()

            if (stepText.isNotBlank()) {
                if (selectedStep != null) {
                    selectedStep!!.name = stepText
                    btnAdd.text = getString(R.string.add_button_text)

                    if (selectedStep!!.stepID > 0) {
                        recipeService.updateStep(selectedStep!!)
                    }

                    btnCancel.visibility = View.GONE

                    selectedStep = null
                }
                else {
                    val step = Step(0, 0, stepText)
                    steps.add(step)
                }

                (lvInstructions.adapter as InstructionAdapter).notifyDataSetChanged()
                setListViewHeightBasedOnChildren(lvInstructions)
                text.text?.clear()
            }
        }

        btnCancel.setOnClickListener {
            text.text?.clear()
            selectedStep = null
            val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(text.windowToken, 0)
            btnCancel.visibility = View.GONE
        }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        Log.e("Listview Size ", "" + listView.count)
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val params = listView.layoutParams
        params.height = (totalHeight
                + listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

    private fun setRecipeValues() {
        root.text_input_name.findViewById<TextView>(R.id.text_input_inputfield)
            .text = recipe!!.name
        root.text_input_descr.findViewById<TextView>(R.id.text_input_inputfield)
            .text = recipe!!.description

        setupDropdownMenus(root.text_input_type, R.array.types, recipe!!.kind)
        setupDropdownMenus(root.text_input_difficulty, R.array.skillLevel, recipe!!.difficulty)

        root.text_input_preptime.findViewById<TextView>(R.id.time_input_inputfield)
            .text = recipe!!.prepMinutes.toString()
        root.text_input_cooktime.findViewById<TextView>(R.id.time_input_inputfield)
            .text = recipe!!.cookMinutes.toString()

        recipe!!.ingredients?.forEach{ ingredient ->
            addChip(ingredient.name)
        }

        val lvInstructions = root.text_input_instructions
            .findViewById<ListView>(R.id.instruction_input_listView)

        steps.addAll(recipe!!.steps!!)
        lvInstructions.adapter = InstructionAdapter(context!!, steps)
        setListViewHeightBasedOnChildren(lvInstructions)
    }

    fun saveRecipe(): Boolean {

        val name = root.text_input_name.findViewById<TextView>(R.id.text_input_inputfield)
            .text.toString().trim()
        val descr = root.text_input_descr.findViewById<TextView>(R.id.text_input_inputfield)
            .text.toString().trim()
        val type = root.text_input_type.findViewById<Spinner>(R.id.dropdown_input_inputfield)
            .selectedItem.toString()
        val difficulty = root.text_input_difficulty.findViewById<Spinner>(R.id.dropdown_input_inputfield)
            .selectedItem.toString()
        val prepTime = root.text_input_preptime.findViewById<TextView>(R.id.time_input_inputfield)
            .text.toString()
        val cookTime = root.text_input_cooktime.findViewById<TextView>(R.id.time_input_inputfield)
            .text.toString()

        val ingredients = root.text_input_ingredients.findViewById<ChipGroup>(R.id.ingredient_input_chipGroup)
            .children.map { chip ->
                Ingredient(0, 0, (chip as Chip).text.toString().trim())
            }.toList()

        //TODO once photos are a thing, insert them here
        val photos = listOf<RecipePhoto>()

        if (!validateRecipe(name, descr, prepTime, cookTime, ingredients)) {
            return false
        }

        if (recipe == null) {
            recipe = Recipe( 0 , name, descr, type, difficulty,
                prepTime.toInt(), cookTime.toInt(), false)
        }
        else {
            val tmpRecipe = Recipe(recipe!!.recipeID , name, descr, type, difficulty,
                prepTime.toInt(), cookTime.toInt(), recipe!!.favourite)

            tmpRecipe.ingredients = recipe!!.ingredients
            tmpRecipe.steps = recipe!!.steps
            tmpRecipe.photos = recipe!!.photos

            recipe = tmpRecipe
        }

        recipeService.addOrUpdateRecipe(recipe!!, ingredients, steps, photos,
            object : DataReadyListener<Long> {

                override fun onDataReady(data: Long?) {
                Log.i("DB", "Successfully inserted Recipe with ID $data")
                activity!!.runOnUiThread {
                    Toast.makeText(context!!, "Saved recipe", Toast.LENGTH_SHORT).show()
                }
                activity!!.finish();
            }
        })

        return true
    }

    private fun validateRecipe(name: String, descr: String, prepTime: String, cookTime: String,
                               ingredients: List<Ingredient>): Boolean {
        var valid = true

        if (name.isBlank()) {
            valid = false
            root.text_input_name.findViewById<TextView>(R.id.text_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }
        else {
            root.text_input_name.findViewById<TextView>(R.id.text_input_description)
                .setTextColor(resources.getColor(R.color.textViewColor, activity!!.theme))
        }
        if (descr.isBlank()) {
            valid = false
            root.text_input_descr.findViewById<TextView>(R.id.text_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }
        else {
            root.text_input_descr.findViewById<TextView>(R.id.text_input_description)
                .setTextColor(resources.getColor(R.color.textViewColor, activity!!.theme))
        }

        if (!prepTime.isBlank() && (prepTime.toInt() >= 0)) {
            root.text_input_preptime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.textViewColor, activity!!.theme))
        }
        else {
            valid = false
            root.text_input_preptime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }

        if (!cookTime.isBlank() && (cookTime.toInt() >= 0)) {
            root.text_input_cooktime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.textViewColor, activity!!.theme))
        }
        else {
            valid = false
            root.text_input_cooktime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }

        if (ingredients.isEmpty()) {
            valid = false
            root.text_input_ingredients.findViewById<TextView>(R.id.ingredient_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }
        else {
            root.text_input_ingredients.findViewById<TextView>(R.id.ingredient_input_description)
                .setTextColor(resources.getColor(R.color.textViewColor, activity!!.theme))
        }

        if (steps.isEmpty()) {
            valid = false
            root.text_input_instructions.findViewById<TextView>(R.id.instruction_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }
        else {
            root.text_input_instructions.findViewById<TextView>(R.id.instruction_input_description)
                .setTextColor(resources.getColor(R.color.textViewColor, activity!!.theme))
        }

        return valid
    }

}

