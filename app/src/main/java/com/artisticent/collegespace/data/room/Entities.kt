package com.artisticent.collegespace.data.room

import androidx.room.*
import com.artisticent.collegespace.domain.models.ContestModel
import com.artisticent.collegespace.domain.models.EventModel
import com.artisticent.collegespace.domain.models.PostModel
import com.artisticent.collegespace.domain.models.UserModel
import com.google.gson.Gson


@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,
    @ColumnInfo(name = "event")
    val event: EventModel
)

@Entity
data class ContestEntity(
    @PrimaryKey(autoGenerate = false)
    val eventId: Int = 0,
    @ColumnInfo(name = "contest")
    val contest: ContestModel
)

class ModelJsonConvertor{
    private val gson = Gson()

    @TypeConverter
    fun fromEventModelToJson(value : EventModel): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun fromJsonToEventModel(value: String): EventModel {
        return gson.fromJson(value, EventModel::class.java)
    }

    @TypeConverter
    fun fromContestModelToJson(value : ContestModel): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun fromJsonToContestModel(value: String): ContestModel {
        return gson.fromJson(value, ContestModel::class.java)
    }

    @TypeConverter
    fun fromUserModelToJson(value: UserModel): String{
        return gson.toJson(value, UserModel::class.java)
    }
    @TypeConverter
    fun fromJsonToUserModel(value: String): UserModel{
        return gson.fromJson(value, UserModel::class.java)
    }

    @TypeConverter
    fun fromPostModelToJson(value: PostModel): String{
        return gson.toJson(value, PostModel::class.java)
    }
    @TypeConverter
    fun fromJsonToPostModel(value: String): PostModel{
        return gson.fromJson(value, PostModel::class.java)
    }
}


@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userNo: Int = 0,
    @ColumnInfo(name = "contest")
    val user: UserModel
)

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    val postTimeId: Long,
    @ColumnInfo(name = "post")
    val post: PostModel
)