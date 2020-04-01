package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import at.tugraz.ist.sw20.mam3.cook.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_add_edit_recipe.view.*
import kotlinx.android.synthetic.main.item_ingredients_input.*

class AddRecipesFragment : Fragment() {

    private lateinit var addRecipesViewModel: AddRecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addRecipesViewModel = ViewModelProvider(this).get(AddRecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_edit_recipe, container, false)
        setViewLables(root)
        val textViewPrepMin: TextView = root.text_input_preptime.findViewById(R.id.time_input_minutes)
        textViewPrepMin.setText(R.string.minutes_text_label)
        val textViewCookMin: TextView = root.text_input_cooktime.findViewById(R.id.time_input_minutes)
        textViewCookMin.setText(R.string.minutes_text_label)

        setupDropdownMenus(root.text_input_type, R.array.types)
        setupDropdownMenus(root.text_input_difficulty, R.array.skillLevel)
        setupIngredients(root)

        return root
    }

    private fun setViewLables(root: View) {
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
    }
    private fun setupDropdownMenus(root: View, arrayList: Int) {
        val skillLevel = resources.getStringArray(arrayList)
        val spinner: Spinner = root.findViewById(R.id.dropdown_input_inputfield)
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, skillLevel)
        spinner.adapter = adapter
    }
    private fun setupIngredients(root: View) {
        val ingredients_input_button: Button = root.findViewById(R.id.ingredient_input_button)
        ingredients_input_button.setOnClickListener {
            val textView: TextView = root.text_input_ingredients.findViewById(R.id.ingredient_input_inputfield)
            // TODO: check for whitespaces only
            if (textView.text.toString() != "") {
                val chip = Chip(context!!)
                chip.text = textView.text
                chip.isClickable = true
                chip.isCheckable = false
                chip.isCloseIconVisible = true

                chip.setOnClickListener {
                    Toast.makeText(context!!, "Clicked: ${chip.text}", Toast.LENGTH_LONG).show()
                }

                chip.setOnCloseIconClickListener {
                    TransitionManager.beginDelayedTransition(ingredient_input_chipGroup)
                    ingredient_input_chipGroup.removeView(chip)
                }

                ingredient_input_chipGroup.addView(chip)
            }
        }
    }
}