package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService

class ImagePreviewAdapter(
    val context : Context,
    val images : List<RecipePhoto>
    ) : BaseAdapter() {

    private val inflater : LayoutInflater = context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val viewHolder : ViewHolder

        if(convertView == null) {
            view = inflater.inflate(R.layout.listitem_instruction, parent, false)

            viewHolder = ViewHolder()
            viewHolder.iv = view.findViewById(R.id.li_image_preview) as ImageView
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val recipePhoto = getItem(position) as RecipePhoto

        viewHolder.iv.setImageURI(RecipeService(context).loadImage(recipePhoto))

        return view
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return images.size
    }

    private class ViewHolder {
        lateinit var iv : ImageView
    }
}
