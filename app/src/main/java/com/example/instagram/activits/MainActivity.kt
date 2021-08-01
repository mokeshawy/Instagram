package com.example.instagram.activits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instagram.R
import com.example.instagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding            : ActivityMainBinding
    lateinit var navHostFragment    : NavHostFragment
    lateinit var navController      : NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // operation work of navigation component.
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController   = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        // show title for action bar on fragment.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)

        // operation work hide and show action bar for fragment.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){

                else -> supportActionBar!!.hide()
            }
        }

        // operation bottom navigation show and hide on fragment.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.homeFragment ->  binding.bottomNavigation.visibility = View.VISIBLE
                else -> binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}