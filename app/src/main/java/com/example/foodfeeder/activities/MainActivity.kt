package com.example.foodfeeder.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.Gravity
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.foodfeeder.R
import com.example.foodfeeder.fragments.FaqFragment
import com.example.foodfeeder.fragments.FavFragment
import com.example.foodfeeder.fragments.HomeFragment
import com.example.foodfeeder.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationView
import java.util.stream.DoubleStream.builder

class MainActivity : AppCompatActivity() {
    lateinit var toolbar : Toolbar
    lateinit var coordinator : CoordinatorLayout
    lateinit var navigator : NavigationView
    lateinit var frame : FrameLayout
    lateinit var drawer : DrawerLayout
    var previousMenuItem : MenuItem? = null
    lateinit var sharedP : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        coordinator = findViewById(R.id.coordinator)
        navigator = findViewById(R.id.navigator)
        frame = findViewById(R.id.frame)
        drawer = findViewById(R.id.drawer)
        sharedP = getSharedPreferences("MySharedP", Context.MODE_PRIVATE)

        setToolbar ()
        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawer, R.string.Open_Drawer, R.string.Close_Drawer)
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigator.setNavigationItemSelectedListener {
            val id = it.itemId

            if(previousMenuItem != null)
            {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(id) {
                R.id.homemenu -> {
                    openHome()
                    drawer.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    drawer.closeDrawers()
                }
                R.id.fav -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavFragment()).commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawer.closeDrawers()
                }
                R.id.faq -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FaqFragment()).commit()
                    supportActionBar?.title = "FAQs"
                    drawer.closeDrawers()
                }
                R.id.logout -> {
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Exit the Appliction ?")
                    dialog.setMessage("You will be logged out!")
                    dialog.setPositiveButton("Continue"){text, listener ->
                        sharedP.edit().putBoolean("loggedin", false).apply()
                        sharedP.edit().clear().apply()
                        val toLogin = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(toLogin)
                    }
                    dialog.setNegativeButton("Stay in app"){text, listener ->
                        openHome()
                        drawer.closeDrawers()
                    }
                    dialog.create().show()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun openHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment()).commit()
        supportActionBar?.title = "Home"
    navigator.setCheckedItem(R.id.homemenu)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =  item?.itemId
        if(id == android.R.id.home)
        {
            drawer.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is HomeFragment -> openHome()
            else -> super.onBackPressed()
        }
    }
}