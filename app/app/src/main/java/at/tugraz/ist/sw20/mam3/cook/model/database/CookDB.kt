package at.tugraz.ist.sw20.mam3.cook.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import at.tugraz.ist.sw20.mam3.cook.model.dao.RecipeDAO
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step

@Database(entities = arrayOf(Recipe::class, Ingredient::class, Step::class), version = 2)
abstract class CookDB : RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO

    companion object {
        var INSTANCE: CookDB? = null

        fun getCookDB(context: Context): CookDB? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    CookDB::class.java, "cookDB")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}
