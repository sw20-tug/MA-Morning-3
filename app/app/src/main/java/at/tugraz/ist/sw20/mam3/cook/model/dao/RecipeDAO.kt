package at.tugraz.ist.sw20.mam3.cook.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step

@Dao
interface RecipeDAO {
    @Query ("SELECT * FROM recipe")
    fun getAllRecipies(): List<Recipe>

    @Query ("SELECT * FROM recipe WHERE favorite = 1")
    fun getFavorites(): List<Recipe>

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Insert
    fun insertStep(step: Step): Long

    @Query ("SELECT * FROM recipe WHERE recipeID = :id")
    fun getRecipeById(id : Long): Recipe
}