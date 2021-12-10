package com.artisticent.collegespace.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.navHostFragment)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item,navController)
        }
        binding.bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED
        binding.bottomNavigationView.selectedItemId = R.id.userFragment

        val dest = intent.getStringExtra("navigation-dest")
        if(dest != null && dest == "edit-fragment") {
            navController.navigate(R.id.userEditFragment)
        }
    }

    override fun onNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onNavigateUp()
    }

}