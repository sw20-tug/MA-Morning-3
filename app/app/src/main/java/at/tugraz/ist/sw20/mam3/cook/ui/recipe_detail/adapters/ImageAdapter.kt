package at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import kotlinx.android.synthetic.main.item_image.view.*

class ImageAdapter(val context : Context, val recipePhotos : List<RecipePhoto>, private val activity: FragmentActivity) :
        RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val image = view.recipe_image
    }

    override fun getItemCount(): Int {
        return recipePhotos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageURI(RecipeService(context).loadImage(recipePhotos[position]))
    }
}