package at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.RecipeDetailActivity
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.item_summarized_recipe.view.*

class RecipeAdapter(private val context : Context, private val recipes : List<Recipe>, private val activity: FragmentActivity) : BaseAdapter() {
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
            viewHolder.favouritesBtn = view.findViewById(R.id.recipe_favourite_star) as ImageButton

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val titleTextView = viewHolder.recipeName
        val favouritesBtn = viewHolder.favouritesBtn
        val typeTextView = viewHolder.type
        val prepTimeTextView = viewHolder.prepTime
        val cookTimeTextView = viewHolder.cookTime
        val titleImageView = viewHolder.image
        val prepTimeIcon = view.recipe_prepare_time.findViewById(R.id.recipe_time_img) as ImageView
        val cookTimeIcon = view.recipe_cook_time.findViewById(R.id.recipe_time_img) as ImageView

        val recipe = getItem(position) as Recipe
        titleTextView.text = recipe.name
        if(recipe.favourite)
            favouritesBtn.setImageResource(R.drawable.ic_star_black_24dp)
            //favouritesBtn.background.state = intArrayOf(android.R.attr.state_pressed)
        typeTextView.text = recipe.kind
        prepTimeTextView.text = recipe.prepMinutes.toString()   // TODO: format correctly
        cookTimeTextView.text = recipe.cookMinutes.toString()   // TODO: format correctly

        titleImageView.setImageResource(R.mipmap.sample_food_tacos_foreground)     // TODO: get correct image id from recipe
        prepTimeIcon.setImageResource(R.drawable.ic_whisk)
        cookTimeIcon.setImageResource(R.drawable.ic_cooking)

        favouritesBtn.setOnClickListener {
            if(recipe.favourite) {
                RecipeService(context).setRecipeFavourite(recipe, false)
                favouritesBtn.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                RecipeService(context).setRecipeFavourite(recipe, true)
                favouritesBtn.setImageResource(R.drawable.ic_star_border_black_24dp)
                //favouritesBtn.background.state = intArrayOf(android.R.attr.state_pressed)
            }

            //val frag : Fragment = activity.nav_host_fragment.
            //activity.supportFragmentManager.beginTransaction().detach(frag).attach(frag).commit()
        }

        // TODO: Load image
        // TODO: Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        view.setOnClickListener {
            val intent = Intent(activity, RecipeDetailActivity::class.java)
            intent.putExtra("recipeID", recipe.recipeID)
            context.startActivity(intent)
        }

        activity.registerForContextMenu(view)
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
        lateinit var favouritesBtn : ImageButton
    }
}