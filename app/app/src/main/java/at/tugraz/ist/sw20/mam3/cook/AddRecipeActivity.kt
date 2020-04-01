package at.tugraz.ist.sw20.mam3.cook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        customizeActionBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_add_edit_recipe, menu)
        return true
    }

    private fun customizeActionBar() {
        supportActionBar?.title = getString(R.string.title_add_recipes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_recipe -> {
                Toast.makeText(this, "Saved recipe", Toast.LENGTH_SHORT).show()
                finish()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}