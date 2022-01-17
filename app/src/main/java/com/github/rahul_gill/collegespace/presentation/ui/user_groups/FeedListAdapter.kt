package com.github.rahul_gill.collegespace.presentation.ui.user_groups

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.databinding.PostItemBinding
import com.github.rahul_gill.collegespace.domain.models.PostModel
import com.bumptech.glide.Glide


private const val ITEM_TYPE_HEADER = 0
private const val ITEM_TYPE_POST = 1
class FeedListAdapter(private val onClickForHeader: (TextView) -> Unit) :
    ListAdapter<RecyclerViewFeedItem, RecyclerView.ViewHolder>(GroupDiffCallback()){



    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_TYPE_POST ->  ViewHolder.from(parent)
            ITEM_TYPE_HEADER -> TextViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder) {
            is ViewHolder -> {
                holder.bind((item as RecyclerViewFeedItem.PostItem).postModel)
            }
            is TextViewHolder ->{
                holder.bind((item as RecyclerViewFeedItem.Header).name, onClickForHeader)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is RecyclerViewFeedItem.Header -> ITEM_TYPE_HEADER
            is RecyclerViewFeedItem.PostItem -> ITEM_TYPE_POST
        }
    }

    fun addHeaderAndSubmitList(list: List<PostModel>?, headerText: String? = null ){
        var items: List<RecyclerViewFeedItem> = listOf()

        if(!headerText.isNullOrEmpty()) { items = items + listOf(RecyclerViewFeedItem.Header(headerText)) }
        if(!list.isNullOrEmpty()) { items = items + list.map { RecyclerViewFeedItem.PostItem(it) } }
        submitList(items)
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
    class TextViewHolder(val view: TextView): RecyclerView.ViewHolder(view) {
        fun bind(name: String, onClickForHeader: (TextView) -> Unit){
            view.text = name
            view.layoutParams = ViewGroup.LayoutParams(0,0)
            view.setOnClickListener { onClickForHeader(it as TextView) }
        }
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.user_group_header, parent, false)
                return TextViewHolder(view as TextView)
            }
        }
    }
    class GroupDiffCallback : DiffUtil.ItemCallback<RecyclerViewFeedItem>(){
        override fun areItemsTheSame(oldItem: RecyclerViewFeedItem, newItem: RecyclerViewFeedItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecyclerViewFeedItem, newItem: RecyclerViewFeedItem): Boolean {
            return oldItem == newItem
        }

    }
}

sealed class RecyclerViewFeedItem{
    class Header(val name: String): RecyclerViewFeedItem()
    data class PostItem(val postModel: PostModel): RecyclerViewFeedItem()

}
