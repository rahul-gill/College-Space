package com.artisticent.collegespace.presentation.ui.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.artisticent.collegespace.R
import com.artisticent.collegespace.domain.models.ContestModel

fun toast(context: Context, message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@BindingAdapter("contest_name")
fun TextView.setContestName(item: ContestModel?){
    item?.let {
        text = it.name
    }
}

@BindingAdapter("contest_time")
fun TextView.setContestTime(item: ContestModel?){
    item?.let {
        text = it.timeString()
    }
}

@BindingAdapter("contest_platform_icon")
fun ImageView.setContestIcon(item: ContestModel?){
    item?.let {
        this.setImageResource(
            when(item.platform){
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
    }
}