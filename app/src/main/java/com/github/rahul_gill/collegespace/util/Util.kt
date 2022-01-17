package com.github.rahul_gill.collegespace.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.rahul_gill.collegespace.R
import com.github.rahul_gill.collegespace.di.GlideApp
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Util{
    fun verifyLoginSignupCredentials(
        username: String? = null,
        email: String? = null,
        password: String? = null,
        confirmPassword: String? = null
    ): String{
        return when{
            email != null && email.isBlank() ->
                "Email can't be blank"
            password != null && password.isBlank() ->
                "Password can't be blank"
            username != null && username.isBlank() ->
                "username can't be blank"
            confirmPassword != null && password != confirmPassword ->
                "Password didn't match"
            else -> ""
        }
    }
    fun toast(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun toLocalDateTime(date: Date): LocalDateTime? {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun toDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault())
                .toInstant());
    }

    fun latestLocalDateTime(date: LocalDateTime, repeatPeriod: Duration): LocalDateTime {
        val current = LocalDateTime.now()
        while (current.isAfter(date)){
            date.plus(repeatPeriod)
        }
        return date
    }
    fun latestLocalDateTime(date: Date, repeatPeriod: Duration? = null): LocalDateTime {
        if(repeatPeriod == null) return toLocalDateTime(date)!!
        val localDateTime = toLocalDateTime(date)
        val current = LocalDateTime.now()
        while (current.isAfter(localDateTime)){
            localDateTime!!.plus(repeatPeriod)
        }
        return localDateTime!!
    }

    @Composable
    fun loadLocalPicture(
        @DrawableRes imageRes: Int
    ): ImageBitmap?{
        var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
        GlideApp.with(LocalContext.current)
            .asBitmap()
            .load(imageRes)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapState = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        return bitmapState?.asImageBitmap()
    }

    @Composable
    fun loadPicture(
        url: String,
        @DrawableRes defaultImage: Int = R.drawable.ic_codeforces
    ): Bitmap? {
        var bitmapState by remember { mutableStateOf<Bitmap?>(null) }
        Glide.with(LocalContext.current)
            .asBitmap()
            .load(defaultImage)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapState = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        Glide.with(LocalContext.current)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    bitmapState = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        return bitmapState
    }
}