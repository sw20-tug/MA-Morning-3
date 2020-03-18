package at.tugraz.ist.sw20.mam3.cook.model.entities

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithIngredientsStepsAndPhotos (
    @Embedded val recipe: Recipe,

    @Relation(
        parentColumn = "recipeID",
        entityColumn = "recipeID"
    )
    val steps: List<Step>,

    @Relation(
        parentColumn = "recipeID",
        entityColumn = "recipeID"
    )
    val ingredients: List<Ingredient>,

    @Relation(
        parentColumn = "recipeID",
        entityColumn = "recipeID"
    )
    val photos: List<RecipePhoto>

)