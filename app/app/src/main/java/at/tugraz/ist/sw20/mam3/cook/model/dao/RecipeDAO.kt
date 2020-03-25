package at.tugraz.ist.sw20.mam3.cook.model.dao

import androidx.room.Dao
import androidx.room.Query
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe

@Dao
interface RecipeDAO {
    @Query ("SELECT * FROM recipe")
    fun getAllRecipies(): List<Recipe>

    @Query ("SELECT * FROM recipe WHERE favorite = 1")
    fun getFavorites(): List<Recipe>
}