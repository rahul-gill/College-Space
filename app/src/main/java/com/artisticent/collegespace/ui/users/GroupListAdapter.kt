package com.artisticent.collegespace.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artisticent.collegespace.databinding.UserGroupItemBinding
import com.artisticent.collegespace.repository.models.UserGroupModel

class GroupListAdapter(private val clickListener: (UserGroupModel) -> Unit) :
    ListAdapter<UserGroupModel, GroupListAdapter.ViewHolder>(GroupDiffCallback()){

    class GroupDiffCallback : DiffUtil.ItemCallback<UserGroupModel>(){
        override fun areItemsTheSame(oldItem: UserGroupModel, newItem: UserGroupModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: UserGroupModel, newItem: UserGroupModel): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(private var binding: UserGroupItemBinding,val clickListener: (UserGroupModel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserGroupModel) {
            binding.userGroupName.text = item.name
            binding.userGroupName.setOnClickListener {
                this.clickListener(item)
            }
        }


        companion object {
            fun from(parent: ViewGroup,clickListener: (UserGroupModel) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserGroupItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding,clickListener)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}