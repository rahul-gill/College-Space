package com.artisticent.collegespace.presentation.ui.contests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artisticent.collegespace.R
import com.artisticent.collegespace.databinding.ContestItemBinding
import com.artisticent.collegespace.domain.models.ContestModel

class ContestListAdapter :
    ListAdapter<ContestModel, ContestListAdapter.ViewHolder>(ContestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }




    class ViewHolder(private var binding: ContestItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ContestModel) {
            item.let{
                binding.imageView.setImageResource(
                    when(it.platform){
                        ContestModel.Platform.CODECHEF -> R.drawable.ic_codechef
                        ContestModel.Platform.LEETCODE -> R.drawable.ic_leetcode
                        ContestModel.Platform.CODEFORCES_GYM -> R.drawable.ic_codeforces
                        ContestModel.Platform.CODEFORCES -> R.drawable.ic_codeforces
                        ContestModel.Platform.TOPCODER -> R.drawable.ic_topcoder
                        ContestModel.Platform.KICKSTART -> R.drawable.ic_google
                        ContestModel.Platform.HACKERRANK -> R.drawable.ic_hackerrank
                        ContestModel.Platform.HACKEREARTH -> R.drawable.ic_hackerearth
                        ContestModel.Platform.CSACADEMY -> R.drawable.ic_csacademy
                        ContestModel.Platform.ATCODER -> R.drawable.ic_atcoder
                        ContestModel.Platform.TOPH -> R.drawable.ic_toph
                    }
                )
                binding.contestName.text = it.name
                binding.contestTime.text = it.timeString()
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContestItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }


    class ContestDiffCallback : DiffUtil.ItemCallback<ContestModel>(){
        override fun areItemsTheSame(oldItem: ContestModel, newItem: ContestModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContestModel, newItem: ContestModel): Boolean {
            return oldItem == newItem
        }

    }


}