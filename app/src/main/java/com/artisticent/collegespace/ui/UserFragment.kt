package com.artisticent.collegespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    lateinit var binding : FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user, container, false)


        return binding.root
    }

}