package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Step(
    @PrimaryKey(autoGenerate = true) var stepID: Long,
    var recipeID: Long,
    var name: String
)