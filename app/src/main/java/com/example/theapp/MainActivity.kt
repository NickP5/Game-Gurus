package com.example.theapp

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.ui.setupWithNavController
import com.example.theapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var hideOptions = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean(KEY_DARK_MODE, false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.SettingsFragment) {
                hideOptions = true
                invalidateOptionsMenu()
            } else {
                hideOptions = false
                invalidateOptionsMenu()
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragment,
                R.id.FriendsFragment,
                R.id.CreateFragment,
                R.id.LeaderboardFragment,
                R.id.ProfileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ProfileFragment -> {
                    val sheet = ProfileFragment()
                    sheet.show(supportFragmentManager, "HalfPageSheet")
                    false
                }
                else -> {
                    findNavController(R.id.nav_host_fragment_content_main)
                        .navigate(item.itemId)
                    true
                }
            }
        }

        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_profile)
        val circular = RoundedBitmapDrawableFactory.create(resources, drawable!!.toBitmap())

        circular.isCircular = true

        val item = binding.bottomNavigationView.menu.findItem(R.id.ProfileFragment)
        item.icon = circular
        item.icon?.setTintList(null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(R.id.SettingsFragment)
                true
            }
            R.id.user_lookup -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(R.id.UserLookupFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.findItem(R.id.action_settings) // Replace action_settings with your item ID
        menuItem.isVisible = !hideOptions
        return super.onPrepareOptionsMenu(menu)
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPref = newBase.getSharedPreferences("settings", MODE_PRIVATE)
        val scale = sharedPref.getFloat("font_scale", 1.0f)

        val context = setFontScale(newBase, scale)
        super.attachBaseContext(context)
    }

    fun setFontScale(context: Context, scale: Float): Context {
        val configuration = context.resources.configuration
        configuration.fontScale = scale
        return context.createConfigurationContext(configuration)
    }
}