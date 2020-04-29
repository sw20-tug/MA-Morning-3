package at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.RecipeDetailActivity
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import kotlinx.android.synthetic.main.item_summarized_recipe.view.*

class RecipeAdapter(val context : Context, val recipes : List<Recipe>, private val activity: FragmentActivity) : BaseAdapter() {
    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder

        if(convertView == null) {
            view = inflater.inflate(R.layout.item_summarized_recipe, parent, false)

            viewHolder = ViewHolder()
            viewHolder.recipeName = view.findViewById(R.id.recipe_title) as TextView
            viewHolder.type = view.findViewById(R.id.recipe_type) as TextView
            viewHolder.prepTime = view.recipe_prepare_time.findViewById(R.id.recipe_time) as TextView
            viewHolder.cookTime = view.recipe_cook_time.findViewById(R.id.recipe_time) as TextView
            viewHolder.image = view.findViewById(R.id.recipe_image) as ImageView

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val titleTextView = viewHolder.recipeName
        val typeTextView = viewHolder.type
        val prepTimeTextView = viewHolder.prepTime
        val cookTimeTextView = viewHolder.cookTime
        val titleImageView = viewHolder.image
        val prepTimeIcon = view.recipe_prepare_time.findViewById(R.id.recipe_time_img) as ImageView
        val cookTimeIcon = view.recipe_cook_time.findViewById(R.id.recipe_time_img) as ImageView

        val recipe = getItem(position) as Recipe
        titleTextView.text = recipe.name
        typeTextView.text = recipe.kind
        prepTimeTextView.text = recipe.prepMinutes.toString()   // TODO: format correctly
        cookTimeTextView.text = recipe.cookMinutes.toString()   // TODO: format correctly

        titleImageView.setImageResource(R.mipmap.sample_food_tacos_foreground)     // TODO: get correct image id from recipe
        prepTimeIcon.setImageResource(R.drawable.ic_whisk)
        cookTimeIcon.setImageResource(R.drawable.ic_cooking)

        // TODO: Load image
        // TODO: Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        view.setOnClickListener {
            val intent = Intent(activity, RecipeDetailActivity::class.java)
            intent.putExtra("recipeID", recipe.recipeID)
            context.startActivity(intent)
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return recipes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return recipes.size
    }

    private class ViewHolder {
        lateinit var recipeName : TextView
        lateinit var type : TextView
        lateinit var prepTime : TextView
        lateinit var cookTime : TextView
        lateinit var image : ImageView
    }
}