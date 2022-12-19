package com.loki.ripoti.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.loki.ripoti.R
import com.loki.ripoti.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navHostController)

        navHostController.addOnDestinationChangedListener{ _, destination, _ ->

            binding.apply {

                when(destination.id) {

                    R.id.homeFragment -> bottomNavigation.visibility = View.VISIBLE
                    R.id.newsFragment -> bottomNavigation.visibility = View.VISIBLE
                    R.id.accountFragment -> bottomNavigation.visibility = View.VISIBLE
                    else -> bottomNavigation.visibility = View.GONE
                }
            }

        }
    }
}