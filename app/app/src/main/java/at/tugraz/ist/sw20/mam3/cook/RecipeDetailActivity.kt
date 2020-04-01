package at.tugraz.ist.sw20.mam3.cook

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_detail_recipe)


    }

    override fun onResume() {
        super.onResume()
        val recipeService = RecipeService(this)
        

        recipeService.getAllRecipes(object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                Log.println(Log.INFO, "Hi this is the special output asdfjölasdflak",
                    data!!.size.toString())
            }
        })
    }
}
