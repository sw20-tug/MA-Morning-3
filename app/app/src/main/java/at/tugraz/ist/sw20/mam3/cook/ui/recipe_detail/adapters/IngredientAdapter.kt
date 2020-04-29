package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Ingredient

class IngredientAdapter(val context : Context, val ingredients : List<Ingredient>, private val activity: FragmentActivity) : BaseAdapter() {
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder

        if(convertView == null) {
            view = inflater.inflate(R.layout.item_instruction, parent, false)

            viewHolder = ViewHolder()
            viewHolder.ingredient = view.findViewById(R.id.recipe_ingredient) as TextView

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val ingredient = getItem(position) as Ingredient
        viewHolder.ingredient.text = ingredient.name

        return view
    }

    override fun getItem(position: Int): Any {
        return ingredients[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return ingredients.size
    }

    private class ViewHolder {
        lateinit var ingredient : TextView
    }
}