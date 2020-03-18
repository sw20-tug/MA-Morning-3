package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.PrimaryKey

data class RecipePhoto(@PrimaryKey(autoGenerate = true) val photoID: Int,
                       val recipeID: Int,
                       val path: String)