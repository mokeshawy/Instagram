package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.instagram.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding            : ActivityMainBinding
    lateinit var navHostFragment    : NavHostFragment
    lateinit var navController      : NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        // operation work of navigation component.
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController   = navHostFragment.navController

        // show title for action bar on fragment.
        appBarConfiguration = AppBarConfiguration(setOf())
        setupActionBarWithNavController(navController,appBarConfiguration)

        // operation work hide and show action bar for fragment.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){

                else -> supportActionBar!!.hide()
            }
        }
    }
}