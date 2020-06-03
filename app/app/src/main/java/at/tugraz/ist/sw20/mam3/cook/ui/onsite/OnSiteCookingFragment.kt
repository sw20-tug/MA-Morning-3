package at.tugraz.ist.sw20.mam3.cook.ui.onsite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.ImageAdapter

class OnSiteCookingFragment: Fragment() {
    private var recipeID: Long? = -1L
    private lateinit var recipeService: RecipeService
    private lateinit var root: View

    private lateinit var recipe: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipeService = RecipeService(requireActivity())
        root = inflater.inflate(R.layout.fragment_on_site_cooking, container, false)
        recipeID = requireActivity().intent?.getLongExtra("recipeID", -1)
        Log.println(Log.INFO, "Parameter", "" + recipeID);
        return root
    }


    override fun onResume() {
        super.onResume()

        if (recipeID != null && recipeID!! > -1L) {
            recipeService.getRecipeById(recipeID!!, object : DataReadyListener<Recipe> {
                override fun onDataReady(data: Recipe?) {
                    recipe = data!!
                    activity!!.runOnUiThread {
                        initElement(data.steps!!)
                    }
                }
            })
        }
    }

    private fun initElement(steps: List<Step>) {
        val tvStepTitle = root.findViewById<TextView>(R.id.tv_onsite_step_title)
        val tvStepContent = root.findViewById<TextView>(R.id.tv_onsite_step_content)
        val btnNext = root.findViewById<Button>(R.id.btn_onsite_next)
        val btnBack = root.findViewById<Button>(R.id.btn_onsite_back)
        val lvImages = root.findViewById<RecyclerView>(R.id.recipe_images)

        lvImages.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        lvImages.isNestedScrollingEnabled = true
        lvImages.setHasFixedSize(false)
        lvImages.adapter = ImageAdapter(context!!, recipe.photos ?: listOf())

        if (steps.size == 1) {
            btnNext.text = resources.getString(R.string.button_text_done)
        }

        btnBack.visibility = View.GONE
        val step0 = steps[0]
        tvStepTitle.text = resources.getString(R.string.lbl_step_title, '1')
        tvStepContent.text = step0.name
        var stepIndex = 1

        btnNext.setOnClickListener {
            ++stepIndex
            if (stepIndex > 0) {
                btnBack.visibility = View.VISIBLE
            }
            else {
                btnBack.visibility = View.GONE
            }

            if (stepIndex <= steps.size) {
                tvStepContent.text = steps[stepIndex - 1].name
                tvStepTitle.text = resources.getString(R.string.lbl_step_title, stepIndex.toString())
            }
            else {
                requireActivity().finish()
            }

            if (stepIndex == steps.size) {
                btnNext.text = resources.getString(R.string.button_text_done)
            }
        }

        btnBack.setOnClickListener {
            --stepIndex
            if (stepIndex <= steps.size - 1) {
                tvStepTitle.text = resources.getString(R.string.lbl_step_title, stepIndex.toString())
                tvStepContent.text = steps[stepIndex - 1].name
            }
            if (stepIndex == 1) {
                btnBack.visibility = View.GONE
            }
            btnNext.text = resources.getString(R.string.button_text_next)
        }
    }
}
