package at.tugraz.ist.sw20.mam3.cook.model.service

import android.content.Context
import android.util.Log
import at.tugraz.ist.sw20.mam3.cook.model.database.CookDB
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step

class RecipeService(private val context: Context) {
    var db: CookDB? = null //getter from singleton we created here

    fun getAllRecipes(callback: DataReadyListener<List<Recipe>>) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val allRecipies = db!!.recipeDao().getAllRecipies()
            callback.onDataReady(allRecipies)
        }).start()
    }

    fun getFavoriteRecipes(callback: DataReadyListener<List<Recipe>>) {
        // Get db instance here
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val allRecipies = db!!.recipeDao().getFavorites()
            callback.onDataReady(allRecipies)
        }).start()
    }

    fun addRecipe(recipe: Recipe, ingredients: List<Ingredient>, steps: List<Step>,
                  callback: DataReadyListener<Long>) {

        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val rID = db!!.recipeDao().insertRecipe(recipe)

            for (ingredient in ingredients) {
                ingredient.recipeID = rID
                db!!.recipeDao().insertIngredient(ingredient)
            }

            for (step in steps) {
                step.recipeID = rID
                db!!.recipeDao().insertStep(step)
            }

            callback.onDataReady(rID)
        }).start()
    }

    fun getRecipeById(id : Long, callback: DataReadyListener<Recipe>) {
        Thread(Runnable {
            db = CookDB.getCookDB(context)
            val recipe = db!!.recipeDao().getRecipeById(id)
            recipe.ingredients = db!!.recipeDao().getIngredientsByRecipeID(id)
            Log.i("INGREDIENTS DATABASE SERVICE", recipe.ingredients.toString())
            recipe.steps = db!!.recipeDao().getStepsByRecipeID(id)
            Log.i("STEPS DATABASE SERVICE", recipe.steps.toString())
            callback.onDataReady(recipe)
        }).start()
    }

}