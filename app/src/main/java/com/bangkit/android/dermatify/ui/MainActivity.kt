package com.bangkit.android.dermatify.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.ActivityMainBinding
import com.bangkit.android.dermatify.util.gone
import com.bangkit.android.dermatify.util.visible

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI(){
        // Bind bottom navigation bar to navigation host fragment (navController)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.apply {
            fabBotNav.setOnClickListener {
                when (navController.currentDestination?.id) {
                    R.id.homeFragment -> {
                        navController.navigate(R.id.action_homeFragment_to_examineFragment)
                    }
                    R.id.journeyFragment -> {
                        navController.navigate(R.id.action_journeyFragment_to_examineFragment)
                    }
                    R.id.learnFragment -> {
                        navController.navigate(R.id.action_learnFragment_to_examineFragment)
                    }
                    R.id.profileFragment -> {
                        navController.navigate(R.id.action_profileFragment_to_examineFragment)
                    }
                }
            }

            navView.setupWithNavController(navController)

            // Clear backstack everytime navigating on bottom nav bar
            navView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        navController.popBackStack(R.id.main_nav, true)
                        navController.navigate(R.id.homeFragment)
                        true
                    }
                    R.id.journeyFragment -> {
                        navController.popBackStack(R.id.main_nav, true)
                        navController.navigate(R.id.journeyFragment)
                        true
                    }
                    R.id.learnFragment -> {
                        navController.popBackStack(R.id.main_nav, true)
                        navController.navigate(R.id.learnFragment)
                        true
                    }
                    R.id.profileFragment -> {
                        navController.popBackStack(R.id.main_nav, true)
                        navController.navigate(R.id.profileFragment)
                        true
                    }
                    else -> false
                }
            }
        }


        // Hide bottom navigation bar on some screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.journeyFragment, R.id.learnFragment, R.id.profileFragment -> {
                    binding.apply {
                        botAppBar.visible()
                        fabBotNav.visible()
                        divider.visible()
                        root.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
                R.id.changeLanguageFragment -> {
                    binding.apply {
                        botAppBar.gone()
                        fabBotNav.visibility = View.GONE
                        divider.gone()
                        root.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
                else -> {
                    binding.apply {
                        botAppBar.gone()
                        fabBotNav.visibility = View.GONE
                        divider.gone()
                    }
                }
            }

        }
    }

}