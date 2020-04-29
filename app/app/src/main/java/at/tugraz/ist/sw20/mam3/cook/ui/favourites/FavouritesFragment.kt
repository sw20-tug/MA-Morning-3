package at.tugraz.ist.sw20.mam3.cook.ui.favourites

import android.app.AlertDialog
import android.content.DialogInterface
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.net.toUri
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.entities.Recipe
import at.tugraz.ist.sw20.mam3.cook.model.service.DataReadyListener
import at.tugraz.ist.sw20.mam3.cook.model.service.RecipeService
import at.tugraz.ist.sw20.mam3.cook.ui.recipes.adapters.RecipeAdapter

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var lvFavorites: ListView
    private lateinit var clickedRecipe: Recipe

    private val RESULT_LOAD_IMAGES = 1
    private val REQUEST_IMAGE_CAPTURE = 2

    private val RECIPE_ID = 1   // TODO: temporary

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
                ViewModelProvider(this).get(FavouritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        val textView: TextView = root.findViewById(R.id.text_favourites)
        favouritesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // TODO: not in favourites fragment
        /*
        val importBtn = root.findViewById<Button>(R.id.button_import_image)
        importBtn?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, RESULT_LOAD_IMAGES)
        }

        val takeBtn = root.findViewById<Button>(R.id.button_take_image)
        takeBtn?.setOnClickListener {
            val intent =  Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
        */
        */

        lvFavorites = root.findViewById(R.id.list_favorites)
        registerForContextMenu(lvFavorites);
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //  TODO: sample code taking pictures and loading from storage
        /*
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            val recipeService = RecipeService(context!!)
            recipeService.storeImageTemporary(imageBitmap)
            img_preview.setImageBitmap(imageBitmap)
            Log.d("Photo", "Take Foto: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()?.size.toString())
        }

        if(requestCode == RESULT_LOAD_IMAGES && resultCode == RESULT_OK) {

            val recipeService = RecipeService(context!!)
            Log.d("Photo", "Before delete: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()?.size.toString())
            recipeService.deleteTemporaryImages()
            Log.d("Photo", "After delete: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()?.size.toString())

            recipeService.storeImageTemporary(data!!.data!!)
            recipeService.storeImageTemporary(data!!.data!!)
            val tempImage = recipeService.storeImageTemporary(data!!.data!!)
            Log.d("Photo", "After add: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()!!.size.toString())


            val readyListener = object : DataReadyListener<Unit> {
                override fun onDataReady(data: Unit?) {
                    Log.d("Photo", "After store: " + File(context!!.filesDir, "recipes").resolve("tmp").listFiles()!!.size.toString())
                    Log.d("Photo", "In store: " + File(context!!.filesDir, "recipes").resolve(1.toString()).listFiles()!!.size.toString())

                    val readyListener = object : DataReadyListener<List<RecipePhoto>> {
                        override fun onDataReady(data: List<RecipePhoto>?) {
                            this@FavouritesFragment.activity!!.runOnUiThread {
                                img_preview.setImageURI(
                                    recipeService.loadImage(
                                        RecipePhoto(
                                            data!!.first().photoID,
                                            data!!.first().recipeID
                                        )
                                    )
                                )
                            }
                        }
                    }
                    recipeService.getAllPhotosFromRecipe(
                        Recipe(1, "", "", "", 0, 0, true), readyListener)
                }
            }

            recipeService.storeImages(1, readyListener)

        }
         */

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouritesViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        val readyListener = object : DataReadyListener<List<Recipe>> {
            override fun onDataReady(data: List<Recipe>?) {
                lvFavorites.adapter = RecipeAdapter(context!!, data ?: listOf())
            }
        }
        RecipeService(context!!).getFavoriteRecipes(readyListener)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        val lv = v as ListView
        val acmi = menuInfo as AdapterView.AdapterContextMenuInfo
        clickedRecipe = lv.getItemAtPosition(acmi.position) as Recipe
        menu.add("Rename")
        menu.add("Edit")
        menu.add("Delete")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.toString().equals("Delete")) {
            val dialogBuilder = AlertDialog.Builder(activity!!)
            dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener{
                    dialog, id ->
                RecipeService(context!!).deleteRecipe(clickedRecipe)
                Toast.makeText(context!!, "deleted" , Toast.LENGTH_LONG).show()
                dialog.dismiss()
            })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id ->
                    dialog.dismiss()
                })
            val alert = dialogBuilder.create()
            alert.setTitle("Are you sure you want to delete the recipe?")
            alert.show()
        }
        return super.onContextItemSelected(item)
    }
}
