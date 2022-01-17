package com.github.rahul_gill.collegespace.presentation.ui.user_groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.databinding.FragmentGroupManageBinding


class GroupManageFragment : Fragment() {
    private lateinit var binding: FragmentGroupManageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_group_manage, container, false)



        return binding.root
    }


}