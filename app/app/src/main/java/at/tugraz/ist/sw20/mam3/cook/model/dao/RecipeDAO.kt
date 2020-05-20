package at.tugraz.ist.sw20.mam3.cook.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipeWithIngredientsStepsAndPhotos
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step

@Dao
interface RecipeDAO {
    @Query ("SELECT * FROM recipe")
    fun getAllRecipies(): List<Recipe>

    @Query ("SELECT * FROM recipe WHERE favourite = 1")
    fun getFavourites(): List<Recipe>

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Delete
    fun deleteRecipe(recipe: Recipe)

    @Update
    fun updateRecipe(recipe: Recipe)

    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Delete
    fun deleteIngredient(ingredient: Ingredient)

    @Insert
    fun insertStep(step: Step): Long

    @Delete
    fun deleteStep(step: Step)

    @Update
    fun updateStep(step: Step)

    @Query ("UPDATE recipe SET favourite = :isFavourite WHERE recipeID = :recipeID")
    fun setRecipeFavourite(recipeID: Long, isFavourite: Boolean)

    @Query ("SELECT * FROM recipePhoto WHERE recipeID = :recipeID" )
    fun getAllPhotosFromRecipe(recipeID: Long): List<RecipePhoto>

    @Insert
    fun insertRecipePhoto(recipePhoto: RecipePhoto): Long

    @Update
    fun updateRecipePhoto(recipePhoto: RecipePhoto)

    @Delete
    fun deleteRecipePhoto(recipePhoto: RecipePhoto)

    @Query ("SELECT * FROM recipe WHERE recipeID = :id")
    fun getRecipeById(id : Long): Recipe

    @Query ("SELECT * FROM step WHERE recipeID = :id")
    fun getStepsByRecipeID(id : Long): List<Step>

    @Query ("SELECT * FROM ingredient WHERE recipeID = :id")
    fun getIngredientsByRecipeID(id : Long): List<Ingredient>
}