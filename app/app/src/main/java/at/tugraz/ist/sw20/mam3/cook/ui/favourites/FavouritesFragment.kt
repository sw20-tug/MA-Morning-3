package at.tugraz.ist.sw20.mam3.cook.ui.favourites

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.sw20.mam3.cook.R
import at.tugraz.ist.sw20.mam3.cook.model.service.ImageService
import kotlinx.android.synthetic.main.fragment_favourites.*
import java.io.File
import java.io.FileOutputStream

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private val RESULT_LOAD_IMAGES = 1

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
        val importBtn = root.findViewById<Button>(R.id.button_import_image)
        importBtn?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, RESULT_LOAD_IMAGES)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RESULT_LOAD_IMAGES && resultCode == RESULT_OK) {
            /*
            Log.d("Save image", "Save image activity result ok")
            Log.d("Save image", "Files dir: " + context?.filesDir.toString())
            //val dir = context?.filesDir.toString() + "/recipes/tmp/"
            val dir = File(context!!.filesDir, "recipes")
            dir.mkdirs()
            val imgName = "$RECIPE_ID.jpg"
            Log.d("Save image", "Image path: $dir")
            //val file = File(context?.getDir(dir, Context.MODE_PRIVATE), "/$RECIPE_ID.jpg")
            //File(imagePath).copyTo(file, true)
            //val bitmap = BitmapFactory.decodeFile(imagePath)
            //val imgDir = context?.getDir(dir, Context.MODE_PRIVATE)

            val imageUri: Uri? = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, imageUri)
            Log.d("Save image", "Created bitmap")

            //val fos = context?.openFileOutput(File(dir, imgName).toString(), Context.MODE_PRIVATE)
            val outFile = File(dir, imgName)
            outFile.createNewFile()
            val fos = FileOutputStream(outFile)
            Log.d("Save image", "Created fos")
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            Log.d("Save image", "Compressed bitmap")
            fos?.flush()
            fos?.close()
            Log.d("Save image", "Closed fos")

            img_preview.setImageURI(Uri.fromFile(File(dir, imgName)))
             */

            img_preview.setImageURI(ImageService.storeImageTemporary(context!!, data!!.data!!))
        }
    }
}
