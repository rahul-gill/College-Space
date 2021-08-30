package com.artisticent.collegespace.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        val adapter = ContestListAdapter()
        binding.contestList.adapter = adapter
        viewModel.contestList.observe(this, {
            adapter.submitList(it)
        })


    }

}