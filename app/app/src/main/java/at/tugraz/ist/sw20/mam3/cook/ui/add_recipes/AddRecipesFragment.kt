package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.RecipesViewModel

class AddRecipesFragment : Fragment() {
    private lateinit var recipesViewModel: AddRecipesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        recipesViewModel =
                ViewModelProvider(this).get(AddRecipesViewModel::class.java)
        // TODO COOK-002A: change fragment layout and remove toast message
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        Toast.makeText(activity, "Hello from the other side", Toast.LENGTH_LONG).show()
        return root
    }
}