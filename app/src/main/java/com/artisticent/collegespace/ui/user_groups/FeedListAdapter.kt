package com.artisticent.collegespace.ui.user_groups

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artisticent.collegespace.databinding.PostItemBinding
import com.artisticent.collegespace.repository.models.PostModel
import com.bumptech.glide.Glide

class FeedListAdapter :
    ListAdapter<PostModel, FeedListAdapter.ViewHolder>(GroupDiffCallback()){

    class GroupDiffCallback : DiffUtil.ItemCallback<PostModel>(){
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(private var binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostModel?) {
            binding.userGroupName.text = item?.user_group_id
            if(item?.text != ""){
                binding.postText.text = item?.text
                Linkify.addLinks(binding.postText,Linkify.ALL)
            }else{
                binding.postText.visibility = View.GONE
            }
            if(item?.imageUrl != "") {
                Glide.with(binding.root).load(item?.imageUrl).into(binding.postImage)
            }else{
                binding.postImage.visibility = View.GONE
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}