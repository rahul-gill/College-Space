package com.artisticent.collegespace.ui.contests

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentContestsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContestsFragment : Fragment() {
    private lateinit var binding: FragmentContestsBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_contests,container,false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = ContestListAdapter()
        binding.contestList.adapter = adapter

        setHasOptionsMenu(true)


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


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contest_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh_menu_item -> viewModel. loadContestDataNew()
            R.id.sort_menu_item -> viewModel.sortContestData()
        }

        return super.onOptionsItemSelected(item)
    }
}