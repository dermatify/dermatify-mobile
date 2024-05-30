package com.bangkit.android.dermatify.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.android.dermatify.R
import com.bangkit.android.dermatify.databinding.ActivityMainBinding
import com.bangkit.android.dermatify.util.hide
import com.bangkit.android.dermatify.util.visible

class MainActivity : AppCompatActivity() {

    //jasmine init
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
        binding.navView.setupWithNavController(navController)

        // Hide bottom navigation bar on some screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.journeyFragment, R.id.learnFragment, R.id.wishlistFragment -> {
                    binding.apply {
                        botAppBar.visible()
                        fabBotNav.visible()
                        divider.visible()
                        root.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
                else -> {
                    binding.apply {
                        botAppBar.hide()
                        fabBotNav.hide()
                        divider.hide()
                    }
                }
            }

        }
    }

}