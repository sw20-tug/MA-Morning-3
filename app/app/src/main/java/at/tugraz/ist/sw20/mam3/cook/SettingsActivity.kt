package at.tugraz.ist.sw20.mam3.cook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.tugraz.ist.sw20.mam3.cook.ui.settings.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_recipes, SettingsFragment())
            .commit()
    }
}