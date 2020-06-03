package at.tugraz.ist.sw20.mam3.cook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.AddRecipesFragment

class OnSiteCookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_site_cooking)

        customizeActionBar()
    }

    private fun customizeActionBar() {
        supportActionBar?.title = getString(R.string.title_on_site_cooking)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
