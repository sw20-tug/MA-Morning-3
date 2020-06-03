package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipePhoto(@PrimaryKey(autoGenerate = true) val photoID: Long,
                       val recipeID: Long,
                       val toDelete: Boolean)