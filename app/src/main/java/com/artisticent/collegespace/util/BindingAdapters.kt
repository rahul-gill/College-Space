package com.artisticent.collegespace.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.artisticent.collegespace.remote.apis.Contest

@BindingAdapter("contest_name")
fun TextView.setContestName( item: Contest?){
    item?.let {
        text = it.name
    }
}

@BindingAdapter("contest_status")
fun TextView.setContestStatus( item: Contest?){
    item?.let {
        text = it.type.lowercase()
    }
}

@BindingAdapter("contest_length")
fun TextView.setContestLength( item: Contest?){
    item?.let {
        text = secondsToHourString(it.durationSeconds)
    }
}

@BindingAdapter("contest_time")
fun TextView.setContestTime( item: Contest?){
    item?.let {
        text = dateFormatter(it.startTimeSeconds)
    }
}