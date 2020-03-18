package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.PrimaryKey

data class Step(@PrimaryKey(autoGenerate = true) val stepID: Int,
                val recipeID: Int,
                val name: String)