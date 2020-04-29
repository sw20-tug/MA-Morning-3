package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Recipe  (
    @PrimaryKey(autoGenerate = true) val recipeID: Long,
    val name: String,
    val description: String,
    val kind: String,
    val difficulty: String,
    val prepMinutes: Int,
    val cookMinutes: Int,
    val favorite: Boolean
    ) {
    @Ignore
    var steps: List<Step>? = null

    @Ignore
    var ingredients: List<Ingredient>? = null

    @Ignore
    var photos: List<RecipePhoto>? = null

}