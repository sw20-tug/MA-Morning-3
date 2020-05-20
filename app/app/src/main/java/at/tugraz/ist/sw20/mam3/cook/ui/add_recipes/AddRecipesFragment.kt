package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters.ImagePreviewAdapter
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters.InstructionAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_edit_recipe.view.*
import kotlinx.android.synthetic.main.item_image_input.view.*
import kotlinx.android.synthetic.main.item_ingredients_input.*
import java.io.File

class AddRecipesFragment : Fragment() {

    private lateinit var addRecipesViewModel: AddRecipesViewModel

    private lateinit var root: View

    private val steps: MutableList<Step> = mutableListOf()

    private lateinit var lvImages : RecyclerView

    private val RESULT_LOAD_IMAGES = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addRecipesViewModel = ViewModelProvider(this).get(AddRecipesViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_add_edit_recipe, container, false)

        lvImages = root.findViewById<RecyclerView>(R.id.image_input_recycler_view)
        lvImages.layoutManager = LinearLayoutManager(context)
        lvImages.isNestedScrollingEnabled = false
        lvImages.setHasFixedSize(true)

        setViewLabels()

        setupDropdownMenus(root.text_input_type, R.array.types)
        setupDropdownMenus(root.text_input_difficulty, R.array.skillLevel)
        setupIngredients()
        setupInstructions()
        setupImages()

        return root
    }

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

    private fun setupDropdownMenus(root: View, arrayList: Int) {
        val skillLevel = resources.getStringArray(arrayList)
        val spinner: Spinner = root.findViewById(R.id.dropdown_input_inputfield)
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, skillLevel)
        spinner.adapter = adapter
    }

    private fun setupIngredients() {
        val ingredientsInputButton: Button = root.findViewById(R.id.ingredient_input_button)
        ingredientsInputButton.setOnClickListener {
            val textView: TextInputEditText = root.text_input_ingredients.findViewById(
                R.id.ingredient_input_inputfield)

            if (textView.text.toString().isNotBlank()) {
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
                    if (ingredient_input_chipGroup.isEmpty()) {
                        ingredient_input_chipGroup.visibility = View.GONE
                    }
                }
                if (ingredient_input_chipGroup.visibility == View.GONE) {
                    ingredient_input_chipGroup.visibility = View.VISIBLE
                }
                ingredient_input_chipGroup.addView(chip)

                textView.text?.clear()
            }
        }
    }

    fun setupInstructions() {

        val lvInstructions = root.text_input_instructions
            .findViewById<ListView>(R.id.instruction_input_listView)

        val btnAdd = root.text_input_instructions
            .findViewById<MaterialButton>(R.id.instruction_input_button)

        lvInstructions.adapter = InstructionAdapter(context!!, steps)

        btnAdd.setOnClickListener {
            val text = root.text_input_instructions
                .findViewById<TextInputEditText>(R.id.instruction_input_inputfield)
            val stepText = text.text.toString()

            if (stepText.isNotBlank()) {
                val step = Step(0, 0, stepText)
                steps.add(step)
                lvInstructions.adapter = InstructionAdapter(context!!, steps)
                setListViewHeightBasedOnChildren(lvInstructions)
                text.text?.clear()
            }
        }
    }
    fun setupImages() {

        val imageAddBtn = root.image_add_image_button
        .findViewById<ImageView>(R.id.image_add_image_button)

        imageAddBtn.setOnClickListener {
            Toast.makeText(context!!, "Clicked add image button", Toast.LENGTH_LONG).show()
            val dialog = AlertDialog.Builder(context)
            val cv = layoutInflater.inflate(R.layout.dialog_add_photo, null) as View
            dialog.setView(cv)
            cv.findViewById<Button>(R.id.dialog_take_picture_button).setOnClickListener {
                Toast.makeText(context!!, "Open Camera" ,Toast.LENGTH_LONG).show()
                val intent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
            cv.findViewById<Button>(R.id.dialog_gallery_button).setOnClickListener {
                Toast.makeText(context!!, "Open Gallery" ,Toast.LENGTH_LONG).show()
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(intent, RESULT_LOAD_IMAGES)
            }

            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //  TODO: sample code taking pictures and loading from storage
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            val recipeService = RecipeService(context!!)
            // TODO: handle edit recipe
            recipeService.storeImageTemporary(imageBitmap)
            // TODO: update gallery
            val dataReadyListener = object : DataReadyListener<List<Uri>> {
                override fun onDataReady(data: List<Uri>?) {
                    activity!!.runOnUiThread {
                        lvImages.adapter = ImagePreviewAdapter(context!!, null, data)
                    }
                    activity!!.finish();
                }
            }
            val allTempImages = recipeService.getAllTempPhotos(dataReadyListener)
            Log.d("Photo", "Take Foto: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()?.size.toString())

        }

        if(requestCode == RESULT_LOAD_IMAGES && resultCode == RESULT_OK) {

            val recipeService = RecipeService(context!!)

            val tempImage = recipeService.storeImageTemporary(data!!.data!!)
            val dataReadyListener = object : DataReadyListener<List<Uri>> {
                override fun onDataReady(data: List<Uri>?) {
                    activity!!.runOnUiThread {
                        lvImages.adapter = ImagePreviewAdapter(context!!, null, data)
                    }
                    activity!!.finish();
                }
            }
            val allTempImages = recipeService.getAllTempPhotos(dataReadyListener)
            Log.d("Photo", "After add: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()!!.size.toString())
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

        if (!validateRecipe(name, descr, prepTime, cookTime, ingredients)) {
            return false
        }

        val recipe = Recipe(0, name, descr, type, difficulty, prepTime.toInt(),
            cookTime.toInt(), false)

        val service = RecipeService(context!!)

        service.addRecipe(recipe, ingredients, steps, object: DataReadyListener<Long> {
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
                .setTextColor(resources.getColor(R.color.primaryTextColor, activity!!.theme))
        }
        if (descr.isBlank()) {
            valid = false
            root.text_input_descr.findViewById<TextView>(R.id.text_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }
        else {
            root.text_input_descr.findViewById<TextView>(R.id.text_input_description)
                .setTextColor(resources.getColor(R.color.primaryTextColor, activity!!.theme))
        }

        if (!prepTime.isBlank() && (prepTime.toInt() >= 0)) {
            root.text_input_preptime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.primaryTextColor, activity!!.theme))
        }
        else {
            valid = false
            root.text_input_preptime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }

        if (!cookTime.isBlank() && (cookTime.toInt() >= 0)) {
            root.text_input_cooktime.findViewById<TextView>(R.id.time_input_description)
                .setTextColor(resources.getColor(R.color.primaryTextColor, activity!!.theme))
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
                .setTextColor(resources.getColor(R.color.primaryTextColor, activity!!.theme))
        }

        if (steps.isEmpty()) {
            valid = false
            root.text_input_instructions.findViewById<TextView>(R.id.instruction_input_description)
                .setTextColor(resources.getColor(R.color.red, activity!!.theme))
        }
        else {
            root.text_input_instructions.findViewById<TextView>(R.id.instruction_input_description)
                .setTextColor(resources.getColor(R.color.primaryTextColor, activity!!.theme))
        }

        return valid
    }

}

