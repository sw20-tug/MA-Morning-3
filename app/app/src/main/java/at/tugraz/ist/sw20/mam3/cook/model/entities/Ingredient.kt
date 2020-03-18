package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.PrimaryKey

data class Ingredient(@PrimaryKey(autoGenerate = true) val ingredientID: Int,
                      val recipeID: Int,
                      val name: String)