package com.artisticent.collegespace.presentation.ui.contests

import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentContestsBinding
import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.presentation.viewmodels.ContestViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContestsFragment : Fragment() {
    private lateinit var binding: FragmentContestsBinding
    private val viewModel: ContestViewModel by viewModels()
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
        binding.contestList.itemAnimator = null
        binding.contestRefreshLayout.setSize(0)

        val contestFilter = binding.contestPlatformFilter
        contestFilter.forEach { child ->
            (child as Chip).setOnCheckedChangeListener{ _, _ ->
                onFilterOrDataChange()
            }
        }

        binding.contestRefreshLayout.setOnRefreshListener {
            viewModel.loadContestDataNew()

        }

        viewModel.contestList.observe(viewLifecycleOwner) {
            onFilterOrDataChange(newData = it)
        }
        viewModel.isLoadingEvents.observe(viewLifecycleOwner){
            binding.contestRefreshLayout.isRefreshing = it
        }



        return binding.root
    }

    private fun onFilterOrDataChange(newData: List<ContestModel>? = null) {
        binding.progressCircular.visibility = View.VISIBLE
        binding.contestList.visibility = View.GONE
        val data = newData ?: viewModel.contestList.value
        val checkedItemIds = binding.contestPlatformFilter.checkedChipIds
        if(checkedItemIds.size == 0){
            adapter.submitList(data)
        }else{
            adapter.submitList(data?.filter {
                for(i in checkedItemIds){
                    if(platformComparator(it.platform,i)) return@filter true
                }
                return@filter false
            })
        }
        lifecycleScope.launch {
            delay(200)
            binding.contestList.layoutManager?.scrollToPosition(0)
            binding.progressCircular.visibility = View.GONE
            binding.contestList.visibility = View.VISIBLE
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

