package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient
import kotlinx.android.synthetic.main.item_ingredient.view.*

class IngredientAdapter(val context : Context, val ingredients : List<Ingredient>) :
        RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {
    // private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val ingredient = view.recipe_ingredient
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredient.text = ingredients[position].name
    }
}