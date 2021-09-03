package com.artisticent.collegespace.ui.contests

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.FragmentContestsBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        viewModel.loadContestCached()




        val adapter = ContestListAdapter()
        binding.contestList.adapter = adapter

        setHasOptionsMenu(true)


        viewModel.contestList.observe(viewLifecycleOwner, {
            Timber.i("my list read")
            adapter.submitList(it)
        })
        viewModel.doneLoading.observe(viewLifecycleOwner,{
            adapter.submitList(viewModel.contestList.value)
        })


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contest_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel. loadContestDataNew()
        return super.onOptionsItemSelected(item)
    }
}