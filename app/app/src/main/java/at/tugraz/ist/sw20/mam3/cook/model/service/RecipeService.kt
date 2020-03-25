package at.tugraz.ist.sw20.mam3.cook.model.service

import android.content.Context
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe

class RecipeService {
    var db: CookDB? = null //getter from singleton we created here

    fun getAllRecipes(callback: DataReadyListener<List<Recipe>>, context: Context) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val allRecipies = db!!.recipeDao().getAllRecipies()
            callback.onDataReady(allRecipies)
        }).start()
    }

}