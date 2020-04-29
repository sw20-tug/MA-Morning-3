package at.tugraz.ist.sw20.mam3.cook

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var listv : MutableList<Recipe> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_recipes, R.id.navigation_favourites))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onResume() {
        super.onResume()
        val recipeService = RecipeService(this)


        //TODO to get test data, comment out this block
        /*
        val r1 = Recipe(0, "Burger", "A basic burger recipe", "Meat",15,
    30, true)
        val r2 = Recipe(0, "Fries", "A basic fries recipe", "Side", 10,
            20, false)

        recipeService.addRecipe(r1, ArrayList(), ArrayList(), object : DataReadyListener<Long> {
            override fun onDataReady(data: Long?) {
                Log.println(Log.INFO, "CookDB", "Inserted r1 - ID: $data")
            }
        })

        recipeService.addRecipe(r2, ArrayList(), ArrayList(), object : DataReadyListener<Long> {
            override fun onDataReady(data: Long?) {
                Log.println(Log.INFO, "CookDB", "Inserted r2 - ID: $data")
            }
        })
        */

        recipeService.getAllRecipes(object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                Log.println(Log.INFO, "Hi this is the special output asdfjölasdflak",
                    data!!.size.toString())
                    listv = data as MutableList<Recipe>
            }
        })
        recipeService.getFavoriteRecipes(object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                Log.println(Log.INFO, "Hi this is the special output asdfjölasdflak",
                    data!!.size.toString())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_filter, menu)

     //   val si = menu?.findItem(R.id.filter) as MenuItem
    //    val more = si.getActionView() as Button

            return true
    }

}

