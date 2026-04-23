package com.example.theapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.ui.setupWithNavController
import com.example.theapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.core.graphics.scale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()
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

        // loggedInUser and get username
        val db = Firebase.firestore
        val docRef = db.collection("users")

        // Profile picture options
        val pfpOptions = listOf(
            "Black Mountain",
            "Mountain",
            "Blue Mountain",
            "Nyan Cat"
        )
        val pfpDrawables = mapOf(
            0 to R.drawable.black_mountain,
            1 to R.drawable.cropped_circle_image,
            2 to R.drawable.blue_mountain,
            3 to R.drawable.nyan_cat
        )

        //Getting name of loggInUser
        docRef.document("$loggedInUser").get()
            .addOnSuccessListener { documentSnapshot ->
                // Load saved profile picture if it exists
                val savedPfp = documentSnapshot.getString("pfp")
                savedPfp?.let {
                    val index = pfpOptions.indexOf(it)
                    if (index >= 0) {
                        viewModel.iconState.value = pfpDrawables[index]
                    }
                }
            }

        viewModel.iconState.observe(this) { iconRes ->
            setCircularProfileImage(iconRes)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appbarLayout) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentContainer) { view, insets ->

            val systemBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBottom
            )

            insets
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.SettingsFragment) {
                hideOptions = true
                invalidateOptionsMenu()
            } else if (destination.id == R.id.UserLookupFragment) {
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
            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(navController.graph.startDestinationId, false)
                .build()

            when (item.itemId) {
                R.id.ProfileFragment -> {
                    val sheet = ProfileFragment()
                    sheet.show(supportFragmentManager, "HalfPageSheet")
                    false
                }

                else -> {
                    if (navController.currentDestination?.id != item.itemId) {
                        navController.navigate(item.itemId, null, options)
                    }
                    true
                }
            }
        }

        navController.addOnDestinationChangedListener { _, _, _ ->

            binding.appbarLayout.setExpanded(true, false)
        }
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
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.SettingsFragment, null, NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build()
                )
                true
            }
            R.id.user_lookup -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.UserLookupFragment, null, NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem1 = menu.findItem(R.id.action_settings)
        val menuItem2 = menu.findItem(R.id.user_lookup)
        menuItem1.isVisible = !hideOptions
        menuItem2.isVisible = !hideOptions
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
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

    fun setCircularProfileImage(icon: Int) {
        val drawable = ContextCompat.getDrawable(this, icon)!!
        val bitmap = drawable.toBitmap()

        val scaledBitmap =
            bitmap.scale((bitmap.width * 1.9).toInt(), (bitmap.height * 1.9).toInt())

        val circular = RoundedBitmapDrawableFactory.create(resources, scaledBitmap)
        circular.isCircular = true

        val item = binding.bottomNavigationView.menu.findItem(R.id.ProfileFragment)
        item.icon = circular
        item.icon?.setTintList(null)
    }
}