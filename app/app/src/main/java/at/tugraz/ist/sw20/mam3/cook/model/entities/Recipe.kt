package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Recipe (
    @PrimaryKey(autoGenerate = true) val recipeID: Int,
    val name: String,
    val description: String,
    val prepMinutes: Int,
    val cookMinutes: Int,

    @Ignore
    val steps: List<Step>,

    @Ignore
    val ingredients: List<Ingredient>
)