package at.tugraz.ist.sw20.mam3.cook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private var listv: MutableList<Recipe> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_recipes, R.id.navigation_favourites
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        val recipeService = RecipeService(this)

        recipeService.getAllRecipes(object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                Log.println(
                    Log.INFO, "Hi this is the special output asdfjölasdflak",
                    data!!.size.toString()
                )
                listv = data as MutableList<Recipe>
            }
        })
        recipeService.getFavouriteRecipes(object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                Log.println(
                    Log.INFO, "Hi this is the special output asdfjölasdflak",
                    data!!.size.toString()
                )
                Log.println(Log.INFO, "DB", "Number of Recipes: ${data.size}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_filter, menu)
        val si = menu?.findItem(R.id.about) as MenuItem
       // val sv = si.getActionView() as TextView

        si.setOnMenuItemClickListener() {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            true
        }
        return true
    }
}
