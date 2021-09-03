package com.artisticent.collegespace.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.artisticent.collegespace.R
import com.artisticent.collegespace.repository.models.ContestModel

@BindingAdapter("contest_name")
fun TextView.setContestName( item: ContestModel?){
    item?.let {
        text = it.name
    }
}

@BindingAdapter("contest_time")
fun TextView.setContestTime( item: ContestModel?){
    item?.let {
        text = it.timeString
    }
}

@BindingAdapter("contest_platform_icon")
fun ImageView.setContestIcon(item: ContestModel?){
    item?.let {
        //will add later for  different platforms
        this.setImageResource(R.drawable.ic_codeforces)
    }
}