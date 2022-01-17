package com.github.rahul_gill.collegespace.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.rahul_gill.collegespace.data.room.entities.*

@Database(
    entities = [ContestEntity::class, PersonalEventEntity::class], //EventEntity::class
    version = 1,
    exportSchema = false
)
@TypeConverters(ModelJsonConverter::class)
abstract class  AppDatabase: RoomDatabase(){
    abstract val eventDatabaseDao: EventDatabaseDao
}