package com.artisticent.collegespace.ui.contests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artisticent.collegespace.databinding.ContestItemBinding
import com.artisticent.collegespace.repository.models.ContestModel

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
            binding.contest = item
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
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ContestModel, newItem: ContestModel): Boolean {
            return oldItem == newItem
        }

    }


}