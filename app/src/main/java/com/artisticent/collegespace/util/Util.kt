package com.artisticent.collegespace.util

import android.content.Context
import android.widget.Toast
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
}