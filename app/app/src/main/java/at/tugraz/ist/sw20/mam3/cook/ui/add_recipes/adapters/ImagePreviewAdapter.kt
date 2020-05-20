package at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.entities.RecipePhoto
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipe_detail.adapters.InstructionAdapter
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter
import kotlinx.android.synthetic.main.item_image_input.view.*
import kotlinx.android.synthetic.main.listitem_image_preview.view.*

class ImagePreviewAdapter(
    val context : Context,
    val imagesRP : MutableList<RecipePhoto>?,
    val imagesUri : MutableList<Uri>?
    ) : RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>() {


    // private val inflater : LayoutInflater = context.getSystemService(
    //    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePreviewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_image_preview, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (imagesRP != null) {
            val recipePhoto = imagesRP[position] as RecipePhoto
            holder.iv.setImageURI(RecipeService(context).loadImage(recipePhoto))
            holder.rp = recipePhoto
        }
        else {
            val imageUri = imagesUri!![position] as Uri
            holder.iv.setImageURI(imageUri)
            holder.uri = imageUri
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return imagesRP?.size ?: imagesUri!!.size
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        val iv = view.li_image_preview
        var rp : RecipePhoto? = null
        var uri : Uri? = null

        init {
            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add("Delete").setOnMenuItemClickListener (MenuItem.OnMenuItemClickListener {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setPositiveButton("Delete") { dialog, id ->
                    val rs = RecipeService(context)
                    if (rp == null) {
                        rs.deleteTemporaryImage(uri!!)
                        imagesUri!!.remove(uri!!)
                    }
                    else {
                        rs.deleteImage(rp!!)
                        imagesRP!!.remove(rp!!)
                    }
                    notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("Are you sure you want to delete the photo?")
                alert.show()
                true
            })


        }


    }


}
