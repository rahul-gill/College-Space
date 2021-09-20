package com.artisticent.collegespace.ui.contests

import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentContestsBinding
import com.artisticent.collegespace.repository.models.ContestModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ContestsFragment : Fragment() {
    private lateinit var binding: FragmentContestsBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter = ContestListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contests,container,false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.contestList.adapter = adapter



        viewModel.contestList.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.doneLoading.observe(viewLifecycleOwner,{
            if(it == true) {
                adapter.submitList(viewModel.contestList.value)
                binding.contestList.smoothScrollToPosition(0)
                viewModel.doneLoadingEventFinish()
            }
        })
        val contestFilter = binding.contestPlatformFilter
        contestFilter.forEach { child ->
            (child as Chip).setOnCheckedChangeListener{ _, _ ->
                registerFilterChange()
            }
        }

        return binding.root
    }

    private fun registerFilterChange() {
        Timber.i("Contest Filter Clicked")
        val checkedItemIds = binding.contestPlatformFilter.checkedChipIds
        if(checkedItemIds.size == 0){
            adapter.submitList(viewModel.contestList.value)
        }else{
            adapter.submitList(viewModel.contestList.value?.filter {
                for(i in checkedItemIds){
                    if(platformComparator(it.platform,i)) return@filter true
                }
                return@filter false
            })
        }
    }

    private fun platformComparator(platform: ContestModel.Platform, id: Int): Boolean {
        if(id == R.id.codeforces_chip && (platform == ContestModel.Platform.CODEFORCES || platform == ContestModel.Platform.CODEFORCES_GYM)){ return true }
        if(id == R.id.codechef_chip && platform == ContestModel.Platform.CODECHEF){ return true }
        if(id == R.id.leetcode_chip && platform == ContestModel.Platform.LEETCODE){ return true }
        if(id == R.id.top_coder_chip && platform == ContestModel.Platform.TOPCODER){ return true }
        if(id == R.id.hacker_earth_chip && platform == ContestModel.Platform.HACKEREARTH){ return true }
        if(id == R.id.hacker_rank_chip && platform == ContestModel.Platform.HACKERRANK){ return true }
        if(id == R.id.google_chip && platform == ContestModel.Platform.KICKSTART){ return true }
        if(id == R.id.at_coder_chip && platform == ContestModel.Platform.ATCODER){ return true }
        if(id == R.id.cs_academy_chip && platform == ContestModel.Platform.CSACADEMY){ return true }
        if(id == R.id.toph_chip && platform == ContestModel.Platform.TOPH){ return true }
        return false
    }
}

