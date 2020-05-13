package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.InstructionAdapter
import kotlinx.android.synthetic.main.item_image_input.view.*
import kotlinx.android.synthetic.main.listitem_image_preview.view.*

class ImagePreviewAdapter(
    val context : Context,
    val imagesRP : List<RecipePhoto>?,
    val imagesUri : List<Uri>?
    ) : RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>() {


    // private val inflater : LayoutInflater = context.getSystemService(
    //    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_image_preview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (imagesRP != null) {
            val recipePhoto = imagesRP[position] as RecipePhoto
            holder.iv.setImageURI(RecipeService(context).loadImage(recipePhoto))
        }
        else {
            val imageUri = imagesUri!![position] as Uri
            holder.iv.setImageURI(imageUri)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return imagesRP?.size ?: imagesUri!!.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val iv = view.li_image_preview
    }
}
