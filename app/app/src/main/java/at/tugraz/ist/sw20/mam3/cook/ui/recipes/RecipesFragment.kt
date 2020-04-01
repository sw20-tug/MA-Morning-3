package at.tugraz.ist.sw20.mam3.cook.ui.recipes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.AddRecipeActivity
import at.tugraz.ist.sw20.mam3.cook.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecipesFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recipes, container, false)
        val textView: TextView = root.findViewById(R.id.text_recipes)
        recipesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val floatingButton: FloatingActionButton = root.findViewById(R.id.item_add_button)
        floatingButton.setOnClickListener {
            val intent = Intent(activity, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}


// val intent = Intent(this, )
//            val fragment = AddRecipesFragment()
//            val fragmentTransaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
