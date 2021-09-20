package com.artisticent.collegespace.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.ActivityMainBinding
import com.artisticent.collegespace.repository.Repository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)



        val navController = this.findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item,navController)
        }
        binding.bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_UNLABELED
        binding.bottomNavigationView.selectedItemId = R.id.userFragment

        lifecycleScope.launch(Dispatchers.IO) {
            repository.loadContestDataFromNetwork()
        }
    }

    override fun onNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onNavigateUp()
    }

}