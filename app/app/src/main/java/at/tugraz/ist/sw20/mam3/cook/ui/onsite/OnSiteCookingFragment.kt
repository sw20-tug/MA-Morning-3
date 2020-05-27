package at.tugraz.ist.sw20.mam3.cook.ui.onsite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService

class OnSiteCookingFragment: Fragment() {
    private var recipeID: Long? = -1L
    private lateinit var recipeService: RecipeService
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipeService = RecipeService(activity!!)
        root = inflater.inflate(R.layout.fragment_on_site_cooking, container, false)
        recipeID = activity!!.intent?.getLongExtra("recipeID", -1)
        Log.println(Log.INFO, "Parameter", "" + recipeID);
        return root
    }

    override fun onResume() {
        super.onResume()
    }

    }